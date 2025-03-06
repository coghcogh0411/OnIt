package com.minho.ownit.member;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.ItemLikeRepo;
import com.minho.ownit.auction.Auction;
import com.minho.ownit.auction.AuctionRepo;
import com.minho.ownit.resale.Resale;
import com.minho.ownit.resale.ResaleRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MemberDAO {
	@Autowired
	private MemberRepo Mrepo;

	@Autowired
	private ResaleRepo rsRepo;

	@Autowired
	private AuctionRepo aRepo;
	
	@Autowired
	private FollowRepo fRepo;
	

	@Autowired
	private ItemLikeRepo ilRepo;

	@Value("${ho.img.folder}")
	private String imgFolder;

	private BCryptPasswordEncoder bcpe;
	private SimpleDateFormat sdf;

	public MemberDAO() {
		bcpe = new BCryptPasswordEncoder();
		sdf = new SimpleDateFormat("yyyyMMdd");
	}

	public Resource getImage(String n) {
		try {
			return new UrlResource("file:" + imgFolder + "/" + n);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Members getMemberToJSON(String m) {
		Optional<Member> members = Mrepo.findById(m);
		List<Member> member = new ArrayList<>();
		if(members.isPresent()) {
			member.add(members.get());
		}
		return new Members(member);
	}
	
	public Member getMemberNicknameToJSON(String m) {
		Member member = Mrepo.findByNickname(m);
		return member;
	}
	
	public void memberReg(Member m, HttpServletRequest req, MultipartFile file) {
		try {
			// 비번 암호화
			String dbPw = bcpe.encode(m.getPw());
			m.setPw(dbPw);

			String year = req.getParameter("year");
			int month = Integer.parseInt(req.getParameter("month"));
			int day = Integer.parseInt(req.getParameter("day"));
			String bd = String.format("%s%02d%02d", year, month, day);
			Date birthday = sdf.parse(bd);
			m.setBirthday(birthday);

			String phone1 = req.getParameter("phone1");
			String phone2 = req.getParameter("phone2");
			String phone3 = req.getParameter("phone3");
			String phone = phone1 + "-" + phone2 + "-" + phone3;
			m.setPhone(phone);

			String addr1 = req.getParameter("addr1");
			String addr2 = req.getParameter("addr2");
			String addr = addr1 + "*" + addr2;
			m.setAddr(addr);

			String fileName = FileNameGenerator.generator(file);
			file.transferTo(new File(imgFolder + "/" + fileName));
			m.setPhoto(fileName);

			if (Mrepo.existsById(m.getId())) {
				throw new Exception();
			}
			Mrepo.save(m);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void memberLogin(Member m, HttpServletRequest req) {
		try {
			Optional<Member> memberTemp = Mrepo.findById(m.getId());
			if (memberTemp.isPresent()) {
				Member dbMember = memberTemp.get();
				if (bcpe.matches(m.getPw(), dbMember.getPw())) {
					req.getSession().setAttribute("loginMember", dbMember);
					req.getSession().setMaxInactiveInterval(10 * 60);
				} else {
					req.setAttribute("result", "로그인 실패(pw오류)");
				}
			} else {
				req.setAttribute("result", "로그인 실패(미가입id)");
			}
		} catch (Exception e) {
			req.setAttribute("result", "로그인 실패(DB)");
		}

	}

	public boolean isLogined(HttpServletRequest req) {
		Member m = (Member) req.getSession().getAttribute("loginMember");
		if (m != null) {
			req.setAttribute("loginPage", "member/logined");
			return true;
		}
		req.setAttribute("loginPage", "member/loginreg");
		return false;
	}

	public void logout(HttpServletRequest req) {
		req.getSession().setAttribute("loginMember", null);
	}

	public void memberPage(HttpServletRequest req, String nickname) {
		Member m = (Member) req.getSession().getAttribute("loginMember");
		if (nickname.equals(m.getNickname())) {
			req.setAttribute("memberprofile", m);
			req.setAttribute("contentPage", "member/memberhome");
			req.setAttribute("myPageContent", "member/update");

		} else {
			// 상대닉네임으로 db조회해서 memberprofile애트리뷰트넘겨주고 상대프로필홈페이지로 보내기
			Member member = Mrepo.findByNickname(nickname);
			req.setAttribute("memberprofile", member);
			req.setAttribute("contentPage", "member/memberhome");
			req.setAttribute("resaleList", rsRepo.findByUser(member));
			req.setAttribute("auctionList", aRepo.findByUser(member));
			req.setAttribute("myPageContent", "member/regproduct");
		}
	}

	public void memberProduct(HttpServletRequest req, String nickname, String f) {
		try {
			Member m = Mrepo.findByNickname(nickname);
			req.setAttribute("memberprofile", m);
			// 로그인되어 있다면 내 페이지 설정
			req.setAttribute("contentPage", "member/memberhome");
			req.setAttribute("myPageContent", "member/regproduct");

			List<Resale> myResales = rsRepo.findByUser(m);
			List<Auction> myAuctions = aRepo.findByUser(m);

			// 필터 값에 따라 분기
			if ("resale".equals(f)) {
				// 중고거래만
				req.setAttribute("resaleList", myResales);
				req.setAttribute("auctionList", null);

			} else if ("start".equals(f)) {
				// 진행 중인 경매만 (예: status="start")
				List<Auction> ongoingAuctions = new ArrayList<>();
				for (Auction a : myAuctions) {
					if ("start".equals(a.getStatus())) {
						ongoingAuctions.add(a);
					}
				}
				req.setAttribute("auctionList", ongoingAuctions);
				req.setAttribute("resaleList", null);

			} else if ("end".equals(f)) {
				// 종료된(역대) 경매만 (예: status="end")
				List<Auction> endedAuctions = new ArrayList<>();
				for (Auction a : myAuctions) {
					if ("end".equals(a.getStatus())) {
						endedAuctions.add(a);
					}
				}
				req.setAttribute("auctionList", endedAuctions);
				req.setAttribute("resaleList", null);

			} else {
				// "all" or 기타 → 전체
				req.setAttribute("resaleList", myResales);
				req.setAttribute("auctionList", myAuctions);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void memberLike(HttpServletRequest req,String nickname, String f) {
		try {
			// 1) 세션에서 로그인한 사용자 가져오기
			Member m = Mrepo.findByNickname(nickname);
			req.setAttribute("memberprofile", m);

			// 3) "내가 좋아요한 상품"을 담을 리스트
			List<Auction> likedAuctions = new ArrayList<>();
			List<Resale> likedResales = new ArrayList<>();

			// (A) 모든 경매 상품 조회 후, 좋아요 여부 체크
			List<Auction> allAuctions = aRepo.findAll();
			for (Auction a : allAuctions) {
				boolean liked = ilRepo.existsByUserIdAndAuctionNo(m, a);
				if (liked) {
					likedAuctions.add(a);
				}
			}

			// (B) 모든 중고거래 상품 조회 후, 좋아요 여부 체크
			List<Resale> allResales = rsRepo.findAll();
			for (Resale r : allResales) {
				boolean liked = ilRepo.existsByUserIdAndResaleNo(m, r);
				if (liked) {
					likedResales.add(r);
				}
			}
			if ("auction".equals(f) || "start".equals(f)) {
				// 경매 중 'start' 상태만 필터링
				List<Auction> ongoingAuctions = new ArrayList<>();
				for (Auction a : likedAuctions) {
					if ("start".equals(a.getStatus())) {
						ongoingAuctions.add(a);
					}
				}
				req.setAttribute("auctionLikeList", ongoingAuctions);
				req.setAttribute("resaleLikeList", null);

			} else if ("end".equals(f)) {
				// 종료된 경매만 필터링
				List<Auction> endedAuctions = new ArrayList<>();
				for (Auction a : likedAuctions) {
					if ("end".equals(a.getStatus())) {
						endedAuctions.add(a);
					}
				}
				req.setAttribute("auctionLikeList", endedAuctions);
				req.setAttribute("resaleLikeList", null);

			} else if ("resale".equals(f)) {
				// 중고거래만 표시
				req.setAttribute("auctionLikeList", null);
				req.setAttribute("resaleLikeList", likedResales);

			} else {
				// "all" or 기타 → 경매 + 중고거래 모두 표시
				req.setAttribute("auctionLikeList", likedAuctions);
				req.setAttribute("resaleLikeList", likedResales);
			}

			// 3) 뷰 페이지 설정
			req.setAttribute("contentPage", "member/memberhome");
			req.setAttribute("myPageContent", "member/memberlike");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Member m, HttpServletRequest req, MultipartFile file) {
		try {
			Member oldmember = (Member) req.getSession().getAttribute("loginMember");
			String id = oldmember.getId();
			m.setId(id);
			m.setNickname(oldmember.getNickname());
			m.setName(oldmember.getName());
			m.setBirthday(oldmember.getBirthday());
			m.setEmail(oldmember.getEmail());

			String dbPw = bcpe.encode(m.getPw());
			m.setPw(dbPw);

			String phone1 = req.getParameter("phone1");
			String phone2 = req.getParameter("phone2");
			String phone3 = req.getParameter("phone3");
			String phone = phone1 + "-" + phone2 + "-" + phone3;
			m.setPhone(phone);

			String addr1 = req.getParameter("addr1");
			String addr2 = req.getParameter("addr2");
			String addr = addr1 + "*" + addr2;
			m.setAddr(addr);

			String oldPhoto = oldmember.getPhoto();
			if (file.isEmpty()) {
				String newPhoto = oldPhoto;
				m.setPhoto(newPhoto);
			} else {
				String fileName = FileNameGenerator.generator(file);
				file.transferTo(new File(imgFolder + "/" + fileName));
				m.setPhoto(fileName);
				new File(oldPhoto).delete();
			}
			Mrepo.save(m);
			req.setAttribute("result", "수정성공");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			req.setAttribute("result", "수정실패");
		}
	}
	
	public void memberfollow(HttpServletRequest req, String nickname) {
		Member member = Mrepo.findByNickname(nickname);
		req.setAttribute("memberprofile", member);
		req.setAttribute("follower", fRepo.findByFollower(member)); 
		req.setAttribute("following", fRepo.findByFollowing(member)); 
	}
	public void memberDelete(HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			Mrepo.delete(m);
			req.setAttribute("result", "탈퇴완료");
		} catch (Exception e) {
			// TODO: handle exception
			req.setAttribute("result", "탈퇴실패");
		}
	}
}
