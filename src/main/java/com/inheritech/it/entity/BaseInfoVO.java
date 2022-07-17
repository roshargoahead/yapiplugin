package com.inheritech.it.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-29 17:44:02
 */
public class BaseInfoVO {
    private Integer add_time;

    private Boolean api_opened;

    private Integer catid;

    private String desc;

    private Integer edit_uid;

    private Integer index;

    private String markdown;

    private String method;

    private String path;

    private Integer project_id;

    private Map<String, Object> query_path;

    private List req_body_form;

    private String req_body_is_json_schema;

    private Map<String, Object> req_body_other;

    private String req_body_type;

    private List req_headers;

    private Map<String, Object> req_params;

    private List req_query;

    private Map<String, Object> res_body;

    private Boolean res_body_is_json_schema;

    private String res_body_type;

    private String status;

    private List tag;

    private String title;

    private String type;

    private Integer uid;

    private Date up_time;

    private String username;

    private Integer __v;

    private Integer _id;

    public Integer getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Integer add_time) {
        this.add_time = add_time;
    }

    public Boolean getApi_opened() {
        return api_opened;
    }

    public void setApi_opened(Boolean api_opened) {
        this.api_opened = api_opened;
    }

    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        this.catid = catid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEdit_uid() {
        return edit_uid;
    }

    public void setEdit_uid(Integer edit_uid) {
        this.edit_uid = edit_uid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Map<String, Object> getQuery_path() {
        return query_path;
    }

    public void setQuery_path(Map<String, Object> query_path) {
        this.query_path = query_path;
    }

    public List getReq_body_form() {
        return req_body_form;
    }

    public void setReq_body_form(List req_body_form) {
        this.req_body_form = req_body_form;
    }

    public String getReq_body_is_json_schema() {
        return req_body_is_json_schema;
    }

    public void setReq_body_is_json_schema(String req_body_is_json_schema) {
        this.req_body_is_json_schema = req_body_is_json_schema;
    }

    public Map<String, Object> getReq_body_other() {
        return req_body_other;
    }

    public void setReq_body_other(Map<String, Object> req_body_other) {
        this.req_body_other = req_body_other;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public List getReq_headers() {
        return req_headers;
    }

    public void setReq_headers(List req_headers) {
        this.req_headers = req_headers;
    }

    public Map<String, Object> getReq_params() {
        return req_params;
    }

    public void setReq_params(Map<String, Object> req_params) {
        this.req_params = req_params;
    }

    public List getReq_query() {
        return req_query;
    }

    public void setReq_query(List req_query) {
        this.req_query = req_query;
    }

    public Map<String, Object> getRes_body() {
        return res_body;
    }

    public void setRes_body(Map<String, Object> res_body) {
        this.res_body = res_body;
    }

    public Boolean getRes_body_is_json_schema() {
        return res_body_is_json_schema;
    }

    public void setRes_body_is_json_schema(Boolean res_body_is_json_schema) {
        this.res_body_is_json_schema = res_body_is_json_schema;
    }

    public String getRes_body_type() {
        return res_body_type;
    }

    public void setRes_body_type(String res_body_type) {
        this.res_body_type = res_body_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getUp_time() {
        return up_time;
    }

    public void setUp_time(Date up_time) {
        this.up_time = up_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }
}