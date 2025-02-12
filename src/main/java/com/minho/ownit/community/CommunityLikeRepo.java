package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.member.Member;

@Repository
public interface CommunityLikeRepo extends CrudRepository<CommunityLike, Integer>{
	
    // 게시물에 대한 좋아요 개수를 조회
	public abstract long countByPost(Community post);
    
    // 특정 게시물(post)에 대한 좋아요 목록을 조회
    public abstract List<CommunityLike> findByPost(Community post);
    
    public abstract CommunityLike findByPostAndUser(Community post, Member user);
    
    // 특정 게시물에 대해 특정 사용자가 좋아요를 눌렀는지 확인
    public abstract boolean existsByPostAndUser(Community post, Member user);
}
