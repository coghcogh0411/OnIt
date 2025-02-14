package com.minho.ownit.auction;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.member.Member;
import com.minho.ownit.resale.Resale;
import com.minho.ownit.resale.ResalePhoto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuctionDAO {
	@Autowired
	private AuctionRepo aRepo;
	@Autowired
	private AuctionPhotoRepo apRepo;
	
	@Value("${ho.img.folder}")
	private String imgFolder;

	public void getAllResaleItems(HttpServletRequest req) {
		req.setAttribute("auctionList", aRepo.findAll());
	}

	public void auctionReg(Auction a, HttpServletRequest req, MultipartFile[] files) {
		String thumbnailFileName = null;
		String fileName = null;
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			a.setUser(m);

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

			aRepo.save(a);

		} catch (Exception e) {
			e.printStackTrace();
			if (thumbnailFileName != null) {
				new File(imgFolder + "/" + thumbnailFileName).delete();
			}
			throw new RuntimeException("상품 등록 실패");
		}
	}
}
