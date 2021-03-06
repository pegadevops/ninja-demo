CREATE TABLE pc_history_ninja_demo
(
  pxaddedbyid              VARCHAR(128),
  pxaddedbysystem          VARCHAR(64),
  pxassignmentelapsedtime  NUMERIC(18),
  pxassignmentpastdeadline NUMERIC(18),
  pxassignmentpastgoal     NUMERIC(18),
  pxcoverinskey            VARCHAR(255),
  pxeffortactualdelta      NUMERIC(18, 3),
  pxflowaction             VARCHAR(32),
  pxhistoryforreference    VARCHAR(255),
  pxinsname                VARCHAR(255),
  pxobjclass               VARCHAR(96),
  pxresolutioncostdelta    NUMERIC(18, 3),
  pxtaskelapsedtime        NUMERIC(18),
  pxtimecreated            TIMESTAMP(6),
  pxtimeflowstarted        VARCHAR(64),
  pyassignedto             VARCHAR(128),
  pyassignedtoworkgroup    VARCHAR(64),
  pyassignmentclass        VARCHAR(96),
  pyflowkey                VARCHAR(255),
  pyflowname               VARCHAR(64),
  pyflowtype               VARCHAR(64),
  pyhistorytype            VARCHAR(1),
  pylabel                  VARCHAR(64),
  pymemo                   VARCHAR(255),
  pymessagekey             VARCHAR(1024),
  pyperformactiontime      NUMERIC(18),
  pyperformassignmenttime  NUMERIC(18),
  pyperformtasktime        NUMERIC(18),
  pyperformer              VARCHAR(128),
  pysubstitutionvalues     VARCHAR(1024),
  pytaskid                 VARCHAR(64),
  pytaskname               VARCHAR(128),
  pyworkclass              VARCHAR(64),
  pzinskey                 VARCHAR(255) NOT NULL,
  pzpvstream               BYTEA,
  pztenantid               VARCHAR(255) NOT NULL,
  pxlatitude               NUMERIC(19, 9),
  pxlongitude              NUMERIC(19, 9),
  pxcommitdatetime         TIMESTAMP(6),
  pxsavedatetime           TIMESTAMP(6),
  CONSTRAINT pc_history_ninja_demo_pk PRIMARY KEY (pzinskey, pztenantid)
);