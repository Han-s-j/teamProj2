package com.project.apt.community.vo;

import java.util.ArrayList;
import java.util.List;

public class ReplyWrapper {
	
	 private ReplyVO parent;
	    private List<ReplyVO> children = new ArrayList<>();

	    public ReplyVO getParent() {
	        return parent;
	    }

	    public void setParent(ReplyVO parent) {
	        this.parent = parent;
	    }

	    public List<ReplyVO> getChildren() {
	        return children;
	    }

	    public void setChildren(List<ReplyVO> children) {
	        this.children = children;
	    }

}
