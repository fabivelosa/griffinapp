create table event (
	eventId integer not null auto_increment,
	cellId integer not null,
	dateTime datetime,
	duration integer not null,
	hier321Id varchar(255),
	hier32Id varchar(255),
	hier3Id varchar(255),
	imsi varchar(255),
	neVersion varchar(255),
	causeCode integer,
	eventCauseId integer,
	failureClass integer,
	countryCode integer,
	operatorCode integer,
	tac integer,
	primary key (eventId)
);

create table eventCause (
	causeCode integer not null,
	eventCauseId integer not null,
	description varchar(255),
	primary key (causeCode,eventCauseId)
);

create table failureClass (
	failureClass integer not null,
	failureDesc varchar(255),
	primary key (failureClass)
);

create table imsiCount (
	imsi varchar(255) not null,
	callFailuresCount bigint not null,
	primary key (imsi)
);

create table imsiSummary (
	imsi varchar(255) not null,
	callFailuresCount bigint not null,
	totalDurationMs bigint not null,
	primary key (imsi)
);

create table mccmnc (
	countryCode integer not null,
	operatorCode integer not null,
	countryDesc varchar(255),
	operatorDesc varchar(255),
	primary key (countryCode,operatorCode)
);

create table phoneModelSummary (
	model varchar(255) not null,
	callFailuresCount bigint not null,
	primary key (model)
);

create table ue (
	tac integer not null,
	accessCapability varchar(255),
	deviceOS varchar(255),
	deviceType varchar(255),
	inputMode varchar(255),
	model varchar(255),
	ueType varchar(255),
	vendorName varchar(255),
	primary key (tac)
);

create table upload (
	uploadID varchar(255) not null,
	reportFile varchar(255),
	totalInvalidRecords integer not null,
	totalValidRecords integer not null,
	uploadStatus integer not null,
	primary key (uploadID)
);

create table users (
	userId varchar(255) not null,
	token varchar(255),
	userName varchar(255),
	userPassword varchar(255),
	userType varchar(255),
	primary key (userId)
);

alter table event
	add constraint FK9ptrqjigns1l1d54baiaqlt66 foreign key (causeCode,eventCauseId)
	references eventCause (causeCode,eventCauseId);

alter table event
	add constraint FKeof45dndl3qmii6o5uu6m2igk foreign key (failureClass)
	references failureClass (failureClass);

alter table event
	add constraint FKkelf4pbph62shnudbyca8fngs foreign key (countryCode,operatorCode)
	references mccmnc (countryCode,operatorCode);

alter table event
	add constraint FK3pg3hygny0mcyrbb3p3eyfvie foreign key (tac)
	references ue (tac);

