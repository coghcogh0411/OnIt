package com.minho.ownit.auction;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.ItemLike;
import com.minho.ownit.ItemLikeRepo;
import com.minho.ownit.member.Member;
import com.minho.ownit.region.Region;
import com.minho.ownit.region.RegionAuction;
import com.minho.ownit.region.RegionAuctionRepo;
import com.minho.ownit.region.RegionMember;
import com.minho.ownit.region.RegionMemberRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuctionDAO {
	@Autowired
	private AuctionRepo aRepo;
	@Autowired
	private AuctionPhotoRepo apRepo;
	@Autowired
	private BidRepo bRepo;
	@Autowired
	private RegionMemberRepo rmRepo;
	@Autowired
	private RegionAuctionRepo regionAuctionRepo;
	@Autowired
	private ItemLikeRepo ilRepo;

	@Value("${ho.img.folder}")
	private String imgFolder;


	public void auctionReg(Auction a, HttpServletRequest req, MultipartFile[] files) {
		String thumbnailFileName = null;
		String fileName = null;
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			a.setUser(m);
			a.setStatus("start");

			if (files != null && files.length > 0) {
				thumbnailFileName = FileNameGenerator.generator(files[0]);
				files[0].transferTo(new File(imgFolder + "/" + thumbnailFileName));

				AuctionPhoto thumbnailPhoto = new AuctionPhoto();
				thumbnailPhoto.setAuction(a);
				thumbnailPhoto.setUrl(thumbnailFileName);
				apRepo.save(thumbnailPhoto);

				a.setThumbnail(thumbnailFileName);
			}
			for (int i = 1; i < files.length; i++) {
				if (files[i] != null && !files[i].isEmpty()) {
					fileName = FileNameGenerator.generator(files[i]);
					files[i].transferTo(new File(imgFolder + "/" + fileName));

					AuctionPhoto auctionPhoto = new AuctionPhoto();
					auctionPhoto.setAuction(a);
					auctionPhoto.setUrl(fileName);

					apRepo.save(auctionPhoto);
				}
			}
			if (a.getDday() != null && a.getDday() > 0) {
                long now = System.currentTimeMillis();
                long plusTime = a.getDday() * 24L * 60L * 60L * 1000L;
                Date endDate = new Date(now + plusTime);
                a.setEnd(endDate);
            }

			aRepo.save(a);
			
			RegionMember regionMember = rmRepo.findByUser(m);
            if (regionMember != null) {
                Region region = regionMember.getRegion();
                RegionAuction regionAuction = new RegionAuction();
                regionAuction.setRegion(region);
                regionAuction.setAuctionNo(a);
                regionAuctionRepo.save(regionAuction);
            } else {
                System.out.println("해당 사용자의 위치정보(region_user)가 DB에 없습니다.");
            }
		} catch (Exception e) {
			e.printStackTrace();
			if (thumbnailFileName != null) {
				new File(imgFolder + "/" + thumbnailFileName).delete();
			}
			throw new RuntimeException("경매 등록 실패");
		}
	}
	
	public void Bid(Bid b, HttpServletRequest req) {
		Member m = (Member) req.getSession().getAttribute("loginMember");
		int auctionNo = Integer.parseInt(req.getParameter("auctionNo"));
		String auctionNoStr = auctionNo + "";
		b.setUser(m);
		b.setBidName(auctionNoStr + m.getNickname());
		System.out.println();
		bRepo.save(b);
		req.setAttribute("bidList", bRepo.findByAuctionNoNoOrderByAmountDesc(auctionNo));
		;
	}
	
	public Bids BidGet(String auctionNo) {

		List<Bid> bids = bRepo.findByAuctionNoNoOrderByAmountDesc(Integer.parseInt(auctionNo));
		return new Bids(bids);
	}
	
	private void closeExpiredAuctions() {
        // (A) 현재 시각
        Date now = new Date();
        // (B) 만료된 경매 조회: "WHERE auction_end <= now AND auction_status='start'"
        List<Auction> expiredList = aRepo.findByEndBeforeAndStatus(now, "start");
        // (C) 상태를 'end'로 바꿔 DB에 저장
        for (Auction auction : expiredList) {
            auction.setStatus("end");
        }
        aRepo.saveAll(expiredList);
    }
	
	public void getAllStartAuctionItems(HttpServletRequest req) {
        // 1) 만료 검사
        closeExpiredAuctions();

        // 2) DB에서 status='start'인 경매 목록
        List<Auction> auctions = aRepo.findByStatus("start");

        // 3) request에 담기
        req.setAttribute("auctionList", auctions);
    }
	
	public void getAllEndAuctionItems(HttpServletRequest req) {
        // 1) 만료 검사 (선택)
        closeExpiredAuctions();

        // 2) DB에서 status='end'인 경매 목록
        List<Auction> auctions = aRepo.findByStatus("end");

        // 3) request에 담기
        req.setAttribute("auctionList", auctions);
    }
	public void getAuctionHome(HttpServletRequest req) {
		
		List<Auction> popularAuctions = aRepo.findByStatus("start");
	    List<Auction> soldAuctions = aRepo.findByStatus("end");

	    req.setAttribute("popularAuctions", popularAuctions);
	    req.setAttribute("soldAuctions", soldAuctions);

	    req.setAttribute("contentPage", "auction/auctionhome");
	}

	public void getAuctionDetail(HttpServletRequest req, int no) {
		try {
			Auction a = aRepo.findById(no).orElse(null);
			req.setAttribute("product", a);
			
			Member m = (Member) req.getSession().getAttribute("loginMember");
			
			boolean isLike = ilRepo.existsByUserIdAndAuctionNo(m, a);
			
			req.setAttribute("isLike", isLike);
			req.setAttribute("photos", apRepo.findByAuction_no(no));
			req.setAttribute("bidList", bRepo.findByAuctionNoNoOrderByAmountDesc(no));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void like(ItemLike il, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			il.setUserId(m);
			int pno = Integer.parseInt(req.getParameter("pno"));
			Auction a = aRepo.findByNo(pno);
			il.setAuctionNo(a);
			il.setResaleNo(null);
			il.setItemType("auction");
			ilRepo.save(il);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void liked(ItemLike il, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			int pno = Integer.parseInt(req.getParameter("pno"));
			Auction a = aRepo.findByNo(pno);
			ItemLike existingLike = ilRepo.findByUserIdAndAuctionNo(m, a);
			ilRepo.delete(existingLike);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void selectBidder(HttpServletRequest req, String bidder, int no) {
		try {
			Auction a = aRepo.findByNo(no);
			Date now = new Date();
			a.setBidder(bidder);
			a.setStatus("end");
			a.setEnd(now);
			aRepo.save(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
