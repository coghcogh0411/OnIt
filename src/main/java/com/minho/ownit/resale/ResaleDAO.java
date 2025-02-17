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
	private ResaleRepo rRepo;

	@Autowired
	private ResalePhotoRepo rpRepo;

	@Autowired
	private ResaleCategoryRepo rcRepo;

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
				rpRepo.save(thumbnailPhoto);
				
				r.setThumbnail(thumbnailFileName);
			}

			
			for (int i = 1; i < files.length; i++) {
				if (files[i] != null && !files[i].isEmpty()) {
					fileName = FileNameGenerator.generator(files[i]);
					files[i].transferTo(new File(imgFolder + "/" + fileName));

					
					ResalePhoto resalePhoto = new ResalePhoto();
					resalePhoto.setResale(r);
					resalePhoto.setUrl(fileName);
					
					rpRepo.save(resalePhoto);
				}
			}

			rRepo.save(r);

		} catch (Exception e) {
			e.printStackTrace();
			if (thumbnailFileName != null) {
				new File(imgFolder + "/" + thumbnailFileName).delete();
			}
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
