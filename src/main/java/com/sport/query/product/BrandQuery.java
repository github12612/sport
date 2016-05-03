package com.sport.query.product;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌查询对象
 * @author chenguanhua
 *
 */
public class BrandQuery {

	private Integer id;
	private String name;
	private String description;
	private String imgUrl;
	private String webSize;
	// 排序:最大最排前
	private Integer sort;
	// 是否可见 1:可见 0:不可见
	private Integer isDisplay;
	
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

	/**
	 * 扩展字段
	 */
	private String fields;//用于${} 直接设置值
	//模糊查询
	private Boolean nameLike;
	
	//当前页
    private Integer pageNo=1;
    
    //页数记录 默认10
    private Integer pageSize=10;
    //数据库索引 页数
    private Integer startRow;
	
	public Boolean getNameLike() {
		return nameLike;
	}
	public void setNameLike(Boolean nameLike) {
		this.nameLike = nameLike;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
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
	
	//order集合
	private List<FieldOrder> fieldOrders=new ArrayList<>();
	//按照id排序+ name asc。。
	public void orderById(Boolean isASC){
		fieldOrders.add(new FieldOrder("id",isASC==true? "asc":"desc"));
	}
	
	//order by 内部类
	public class FieldOrder{
		
		private String field;//id ,name,img_url
		private String order;//asc,desc
		
		public FieldOrder() {}
		
		public FieldOrder(String field, String order) {
			super();
			this.field = field;
			this.order = order;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getOrder() {
			return order;
		}
		public void setOrder(String order) {
			this.order = order;
		}
		
	}
}
