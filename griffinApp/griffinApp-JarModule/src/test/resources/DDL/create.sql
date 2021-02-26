create table event (
	eventId integer not null auto_increment,
	cellId integer not null,
	dateTime tinyblob,
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
	ueType integer,
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

create table mccmnc (
	countryCode integer not null,
	operatorCode integer not null,
	countryDesc varchar(255),
	operatorDesc varchar(255),
	primary key (countryCode,operatorCode)
);

create table ue (
	ueType integer not null,
	accessCapability varchar(255),
	deviceOS varchar(255),
	deviceType varchar(255),
	inputModel varchar(255),
	manufacturer varchar(255),
	marketingName varchar(255),
	model varchar(255),
	vendorName varchar(255),
	primary key (ueType)
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
	add constraint FKlbwtjlcl6k4lwwg99lmkukayx foreign key (ueType)
	references ue (ueType);

