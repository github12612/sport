package com.sport.bean.product;

import com.sport.web.Constants;

/**
 * 品牌
 * @author chenguanhua
 *
 */
public class Brand {

    private Integer id;
    private String name;
    private String description;
    private String imgUrl;
    private String webSize;
    //排序:最大最排前
    private Integer sort;
    //是否可见 1:可见 0:不可见
    private Integer isDisplay;
	//当前页
    private Integer pageNo=1;
    
    //页数记录 默认10
    private Integer pageSize=10;
    //索引 页数
    private Integer startRow;
    
    //获取服务器图片路径
    public String getAllUrl(){
    	return Constants.IMG_URL+imgUrl;
    }
    
    public Brand() {}

	public Brand(Integer id, String name, String description, String imgUrl, String webSize, Integer sort,
			Integer isDisplay) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imgUrl = imgUrl;
		this.webSize = webSize;
		this.sort = sort;
		this.isDisplay = isDisplay;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getWebSize() {
		return webSize;
	}

	public void setWebSize(String webSize) {
		this.webSize = webSize;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", description=" + description + ", imgUrl=" + imgUrl
				+ ", webSize=" + webSize + ", sort=" + sort + ", isDisplay=" + isDisplay + "]";
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		//索引
		this.startRow=(pageNo-1)*pageSize;
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		//索引
		this.startRow=(pageNo-1)*pageSize;
		this.pageSize = pageSize;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	} 
    

}
