package com.example.wing.workingsongpa.CourseList;

import com.example.wing.workingsongpa.ApplicationClass;

public class SectionItem implements ApplicationClass.Item {

	private final String title;
	
	public SectionItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

}
