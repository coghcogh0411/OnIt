package com.minho.ownit.resale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;
import com.minho.ownit.member.Member;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ResaleDAO {
	@Autowired
	private ResaleRepo Rrepo;

	@Autowired
	private ResalePhotoRepo RPrepo;

	@Autowired
	private ResaleCategoryRepo RCrepo;

	@Value("${ho.img.folder}")
	private String imgFolder;

	public void resaleReg(Resale r, HttpServletRequest req, MultipartFile[] files) {
		String thumbnailFileName = null;
		String fileName = null;
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			r.setUser(m);

			
			if (files != null && files.length > 0) {
				thumbnailFileName = FileNameGenerator.generator(files[0]);
				files[0].transferTo(new File(imgFolder + "/" + thumbnailFileName));
				
				ResalePhoto thumbnailPhoto = new ResalePhoto();
				thumbnailPhoto.setResale(r);
				thumbnailPhoto.setUrl(thumbnailFileName);
				RPrepo.save(thumbnailPhoto);
				
				r.setThumbnail(thumbnailFileName);
			}

			
			for (int i = 1; i < files.length; i++) {
				if (files[i] != null && !files[i].isEmpty()) {
					fileName = FileNameGenerator.generator(files[i]);
					files[i].transferTo(new File(imgFolder + "/" + fileName));

					
					ResalePhoto resalePhoto = new ResalePhoto();
					resalePhoto.setResale(r);
					resalePhoto.setUrl(fileName);
					
					RPrepo.save(resalePhoto);
				}
			}

			Rrepo.save(r);

		} catch (Exception e) {
			e.printStackTrace();
			if (thumbnailFileName != null) {
				new File(imgFolder + "/" + thumbnailFileName).delete();
			}
			throw new RuntimeException("상품 등록 실패");
		}
	}

	public List<ResaleCategory> getAllCategories() {
		return RCrepo.findAll();
	}
}
