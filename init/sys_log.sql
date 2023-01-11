CREATE TABLE `sys_log`  (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `request_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作IP',
                            `type` int(1) NULL DEFAULT NULL COMMENT '日志类型 1:操作类型 2:异常类型',
                            `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '标题',
                            `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作描述',
                            `request_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求地址',
                            `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
                            `finish_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
                            `consuming_time` bigint(20) NULL DEFAULT NULL COMMENT '消耗时间',
                            `ua` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                            `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                            `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人用户id',
                            `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人用户id',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

CREATE TABLE `sys_log_ext`  (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `log_id` bigint(20) NOT NULL COMMENT '日志id',
                                `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
                                `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回值',
                                `ex_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常描述',
                                `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人用户id',
                                `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人用户id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志扩展' ROW_FORMAT = DYNAMIC;
