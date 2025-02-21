package com.minho.ownit.resale;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.member.Member;
import com.minho.ownit.region.Region;
import com.minho.ownit.region.RegionMember;
import com.minho.ownit.region.RegionMemberRepo;
import com.minho.ownit.region.RegionRepo;
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
		List<Resale> allItems = (List<Resale>) rRepo.findAll();
		req.setAttribute("resaleList", allItems);
		req.setAttribute("categorytitle", null);
	}

	public void getResaleByCategory(HttpServletRequest req, int no) {
		try {
			req.getSession().setAttribute("category", no);
			ResaleCategory category = rcRepo.findById(no).orElse(null);
			req.setAttribute("categorytitle", category);
			List<Resale> resaleList = rRepo.findByCategory_No(no);
			req.setAttribute("resaleList", resaleList);
			req.setAttribute("category", rcRepo.findAll());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getResaleDetail(HttpServletRequest req, int no) {
		try {

			Resale r = rRepo.findById(no).orElse(null);
			req.setAttribute("product", r);

			List<ResalePhoto> photos = rpRepo.findByResale_no(no);
			req.setAttribute("photos", photos);
			req.setAttribute("category", rcRepo.findAll());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
