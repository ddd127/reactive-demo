/*
 * This file is generated by jOOQ.
 */
package com.example.reactive.demo.domain.db;


import com.example.reactive.demo.domain.db.tables.FlywaySchemaHistory;
import com.example.reactive.demo.domain.db.tables.TestEntity;
import com.example.reactive.demo.domain.db.tables.records.FlywaySchemaHistoryRecord;
import com.example.reactive.demo.domain.db.tables.records.TestEntityRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<TestEntityRecord> TEST_ENTITY_PKEY = Internal.createUniqueKey(TestEntity.TEST_ENTITY, DSL.name("test_entity_pkey"), new TableField[] { TestEntity.TEST_ENTITY.ID }, true);
}
