package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends CrudRepository<Community, Integer> {
	// 카테고리별 게시글을 조회 (페이지네이션 적용)
	public abstract List<Community> findByCategory_CategoryNo(Integer categoryNo, Pageable pageable);

	// 카테고리별 게시글 수를 조회
	public abstract Long countByCategory_CategoryNo(Integer categoryNo);

	// 제목 또는 작성자 ID를 포함하여 카테고리로 필터링된 게시글을 조회 (페이지네이션 적용)
	public abstract List<Community> findByCategory_CategoryNoAndTitleContainingOrWriterIdContaining(Integer categoryNo,
			String title, String id, Pageable pageable);

	// 제목 또는 작성자 ID를 포함하여 카테고리로 필터링된 게시글 수를 조회
	public abstract Long countByCategory_CategoryNoAndTitleContainingOrWriterIdContaining(Integer categoryNo,
			String title, String id);
}
