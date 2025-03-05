package com.minho.ownit.resale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.ItemLike;
import com.minho.ownit.ItemLikeRepo;
import com.minho.ownit.member.Follow;
import com.minho.ownit.member.FollowRepo;
import com.minho.ownit.member.Member;
import com.minho.ownit.member.MemberRepo;
import com.minho.ownit.region.Region;
import com.minho.ownit.region.RegionMember;
import com.minho.ownit.region.RegionMemberRepo;
import com.minho.ownit.region.RegionResale;
import com.minho.ownit.region.RegionResaleRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ResaleDAO {
	@Autowired
	private ResaleRepo rRepo;

	@Autowired
	private ResalePhotoRepo rpRepo;

	@Autowired
	private ResaleCategoryRepo rcRepo;

	@Autowired
	private RegionResaleRepo rrRepo;

	@Autowired
	private RegionMemberRepo rmRepo;

	@Autowired
	private ItemLikeRepo ilRepo;

	@Autowired
	private MemberRepo mRepo;
	
	@Autowired
	private FollowRepo fRepo;
	
	@Value("${ho.img.folder}")
	private String imgFolder;

	public void resaleReg(Resale r, HttpServletRequest req, MultipartFile[] files) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			if (m == null) {
				throw new RuntimeException("로그인 정보가 없습니다.");
			}
			r.setUser(m);

			// 1) Resale 먼저 DB에 저장 (PK 생성)
			rRepo.save(r);

			// 2) 첫 번째 파일(썸네일)을 찾았는지 여부
			boolean thumbnailSet = false;

			// 3) 모든 파일을 순회
			for (int i = 0; i < files.length; i++) {
				MultipartFile mf = files[i];
				if (mf != null && !mf.isEmpty()) {
					// 파일명 생성 + 로컬에 저장
					String fileName = FileNameGenerator.generator(mf);
					mf.transferTo(new File(imgFolder + "/" + fileName));

					// 아직 썸네일이 설정되지 않았다면 → 첫 파일을 썸네일로
					if (!thumbnailSet) {
						r.setThumbnail(fileName);
						rRepo.save(r); // 썸네일 정보 업데이트

						// (선택) 썸네일도 Photo 테이블에 넣고 싶다면:
						ResalePhoto rp = new ResalePhoto();
						rp.setResale(r);
						rp.setUrl(fileName);
						rpRepo.save(rp);

						thumbnailSet = true;
					} else {
						// 나머지 파일은 일반 사진으로 저장
						ResalePhoto rp = new ResalePhoto();
						rp.setResale(r);
						rp.setUrl(fileName);
						rpRepo.save(rp);
					}
				}
			}
			RegionMember regionMember = rmRepo.findByUser(m);
			if (regionMember != null) {
				// region_user에 저장된 지역 정보를 가져와서 region_resale에 저장
				Region region = regionMember.getRegion();
				RegionResale regionResale = new RegionResale();
				regionResale.setRegion(region);
				regionResale.setResaleNo(r);
				rrRepo.save(regionResale);
			} else {
				System.out.println("해당 사용자의 위치정보(region_user)가 DB에 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("상품 등록 실패");
		}

	}

	public void getAllCategories(HttpServletRequest req) {
		req.setAttribute("category", rcRepo.findAll());
	}

	public void getAllResaleItems(HttpServletRequest req) {
		List<Resale> items = new ArrayList<>();

		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			if (m != null) {
				// (A) 로그인된 사용자
				RegionMember regionMember = rmRepo.findByUser(m);
				if (regionMember != null) {
					// region_user에 있는 regionName으로 필터
					String regionName = regionMember.getRegion().getName();
					List<RegionResale> rrList = rrRepo.findByRegion_Name(regionName);
					for (RegionResale rr : rrList) {
						items.add(rr.getResaleNo());
					}
				} else {
					// region_user 정보가 없으면 전체 상품
					items = (List<Resale>) rRepo.findAll();
				}
			} else {
				// (B) 로그인 안 됨 => 세션 regionSession 확인
				String regionSearch = (String) req.getSession().getAttribute("regionSessionSearch");
				if (regionSearch != null && !regionSearch.isEmpty()) {
					List<RegionResale> rrList = rrRepo.findByRegion_Name(regionSearch);
					for (RegionResale rr : rrList) {
						items.add(rr.getResaleNo());
					}
				} else {
					// regionSession도 없으면 전체 상품
					items = (List<Resale>) rRepo.findAll();
				}
			}

			// 결과를 request에 저장
			req.setAttribute("resaleList", items);
			// 카테고리 목록
			req.setAttribute("category", rcRepo.findAll());
			req.setAttribute("categorytitle", null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 카테고리별 조회
	public void getResaleByCategory(HttpServletRequest req, int no) {
		try {
			req.getSession().setAttribute("category", no);
			ResaleCategory category = rcRepo.findById(no).orElse(null);
			req.setAttribute("categorytitle", category);

			List<Resale> items = new ArrayList<>();

			Member m = (Member) req.getSession().getAttribute("loginMember");
			if (m != null) {
				// 로그인된 사용자 => region_user 확인
				RegionMember regionMember = rmRepo.findByUser(m);
				if (regionMember != null) {
					String regionName = regionMember.getRegion().getName();
					List<RegionResale> rrList = rrRepo.findByRegion_Name(regionName);
					// rrList에 있는 상품 중 카테고리 no와 일치하는 것만 필터
					for (RegionResale rr : rrList) {
						Resale r = rr.getResaleNo();
						if (r.getCategory().getNo() == no) {
							items.add(r);
						}
					}
				} else {
					// region_user 없음 => 전체 상품 중 카테고리 no
					items = rRepo.findByNo(no);
				}
			} else {
				// 로그인 안 됨 => 세션 regionSession 확인
				String regionSearch = (String) req.getSession().getAttribute("regionSessionSearch");
				if (regionSearch != null && !regionSearch.isEmpty()) {
					// regionSession으로 필터
					List<RegionResale> rrList = rrRepo.findByRegion_Name(regionSearch);
					for (RegionResale rr : rrList) {
						Resale r = rr.getResaleNo();
						if (r.getCategory().getNo() == no) {
							items.add(r);
						}
					}
				} else {
					// 세션 regionSession도 없으면 전체 상품 중 카테고리 no
					items = rRepo.findByNo(no);
				}
			}

			req.setAttribute("resaleList", items);
			req.setAttribute("category", rcRepo.findAll());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getResaleDetail(HttpServletRequest req, int no) {
		try {
			Resale r = rRepo.findById(no).orElse(null);
			req.setAttribute("product", r);

			Member m = (Member) req.getSession().getAttribute("loginMember");
			boolean isLike = ilRepo.existsByUserIdAndResaleNo(m, r);
			req.setAttribute("isLike", isLike);

			List<ResalePhoto> photos = rpRepo.findByResale_no(no);
			req.setAttribute("photos", photos);
			req.setAttribute("category", rcRepo.findAll());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDisplayRegion(HttpServletRequest req) {
		Member m = (Member) req.getSession().getAttribute("loginMember");
		String displayRegion = "지역 없음";

		if (m != null) {
			// 로그인 사용자
			RegionMember regionMember = rmRepo.findByUser(m);
			if (regionMember != null) {
				// region_user → region.getFirstName()+", "+region.getSecondName() 등
				Region region = regionMember.getRegion();
				if (region != null) {
					// 예: region.getFirstName()="광주광역시", region.getSecondName()="서구"
					displayRegion = region.getFirstName() + ", " + region.getSecondName();
				}
			} else {
				// region_user가 없으면 비로그인 표시용과 동일하게 처리
				String sessionDisplay = (String) req.getSession().getAttribute("regionSessionDisplay");
				if (sessionDisplay != null && !sessionDisplay.isEmpty()) {
					displayRegion = sessionDisplay;
				}
			}
		} else {
			// 비로그인
			String sessionDisplay = (String) req.getSession().getAttribute("regionSessionDisplay");
			if (sessionDisplay != null && !sessionDisplay.isEmpty()) {
				displayRegion = sessionDisplay;
			}
		}

		req.setAttribute("displayRegion", displayRegion);
	}

	public void like(ItemLike il, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			il.setUserId(m);
			int pno = Integer.parseInt(req.getParameter("pno"));
			Resale r = rRepo.findById(pno).orElse(null);
			il.setResaleNo(r);
			il.setAuctionNo(null);
			il.setItemType("resale");
			ilRepo.save(il);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void liked(ItemLike il, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			int pno = Integer.parseInt(req.getParameter("pno"));
			Resale r = rRepo.findById(pno).orElse(null);
			ItemLike existingLike = ilRepo.findByUserIdAndResaleNo(m, r);
			ilRepo.delete(existingLike);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void resaleFollow(HttpServletRequest req, String follower, String following) {
		try {
			Follow f= new Follow();
			Member f1 = mRepo.findByNickname(follower);
			Member f2 = mRepo.findByNickname(following);
			f.setFollower(f1);
			f.setFollowing(f2);
			fRepo.save(f);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
