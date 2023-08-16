package com.example.springframe;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.springframe.entity.SysPermission;
import com.example.springframe.license.License;
import com.example.springframe.license.LicenseCreator;
import com.example.springframe.utils.tree.TreeUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TreeTest {
    @Test
    void generateTree() {
        String str1= """
                {
                      "id": 2,
                      "code": "e54d063f-2132-43d3-aaf7-cdb0508a737c",
                      "type": 0,
                      "name": "草稿箱",
                      "description": "平台草稿箱菜单",
                      "pid": 0,
                      "menuType": 0,
                      "keystr": "platform_case_list",
                      "routing": "/drafts",
                      "sort": -1,
                      "children": []
                    }
                """;
        String str2= """
                {
                          "id": 28,
                          "code": "97ebbdb6-9640-419a-91ea-e87e9208443b",
                          "type": 0,
                          "name": "查看",
                          "description": "平台草稿箱详情按钮",
                          "pid": 2,
                          "menuType": 1,
                          "keystr": "platform_case_details",
                          "routing": "/drafts/detail",
                          "sort": 0,
                          "children": []
                        }
                """;
        String str3= """
                {
                          "id": 29,
                          "code": "8f9c6496-a4da-4312-a247-0cd3f45f45dc",
                          "type": 0,
                          "name": "新增",
                          "description": "平台草稿箱创建按钮",
                          "pid": 28,
                          "menuType": 1,
                          "keystr": "platform_case_update",
                          "routing": null,
                          "sort": 2,
                          "children": []
                        }
                """;
        String str4= """
                 {
                          "id": 30,
                          "code": "901afad3-2464-4ad6-b2af-84e9c3e6ed04",
                          "type": 0,
                          "name": "修改",
                          "description": "平台草稿箱编辑按钮",
                          "pid": 28,
                          "menuType": 1,
                          "keystr": "platform_case_update",
                          "routing": "/drafts/edit",
                          "sort": 4,
                          "children": []
                        }
                """;
        String str5= """
                {
                          "id": 31,
                          "code": "db704d1b-14d3-42cb-a53d-e4894a6b63ad",
                          "type": 0,
                          "name": "删除",
                          "description": "平台草稿箱删除按钮",
                          "pid": 30,
                          "menuType": 1,
                          "keystr": "platform_case_delete",
                          "routing": null,
                          "sort": 3,
                          "children": []
                        }
                """;
        String str6= """
                 {
                          "id": 32,
                          "code": "901afad3-2464-4ad6-b2af-84e9c3e6ed04",
                          "type": 0,
                          "name": "修改1",
                          "description": "平台草稿箱编辑按钮",
                          "pid": 28,
                          "menuType": 1,
                          "keystr": "platform_case_update",
                          "routing": "/drafts/edit",
                          "sort": 1,
                          "children": []
                        }
                """;
        String str7= """
                 {
                          "id": 33,
                          "code": "901afad3-2464-4ad6-b2af-84e9c3e6ed04",
                          "type": 0,
                          "name": "修改2",
                          "description": "平台草稿箱编辑按钮",
                          "pid": 28,
                          "menuType": 1,
                          "keystr": "platform_case_update",
                          "routing": "/drafts/edit",
                          "sort": 3,
                          "children": []
                        }
                """;
        String str8= """
                 {
                          "id": 34,
                          "code": "901afad3-2464-4ad6-b2af-84e9c3e6ed04",
                          "type": 0,
                          "name": "修改3",
                          "description": "平台草稿箱编辑按钮",
                          "pid": 28,
                          "menuType": 1,
                          "keystr": "platform_case_update",
                          "routing": "/drafts/edit",
                          "sort": 5,
                          "children": []
                        }
                """;
        List<SysPermission> list =new ArrayList<>();
        list.add(JSONUtil.toBean(str1, SysPermission.class));
        list.add(JSONUtil.toBean(str2, SysPermission.class));
        list.add(JSONUtil.toBean(str3, SysPermission.class));
        list.add(JSONUtil.toBean(str4, SysPermission.class));
        list.add(JSONUtil.toBean(str5, SysPermission.class));
        list.add(JSONUtil.toBean(str6, SysPermission.class));
        list.add(JSONUtil.toBean(str7, SysPermission.class));
        list.add(JSONUtil.toBean(str8, SysPermission.class));
        JSONArray objects = TreeUtils.genListTreeByExtend(list);
        System.out.println(objects);
    }
}
