package com.inheritech.it.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend.lib.annotations.Data;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-29 17:44:02
 */
@Data
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
}