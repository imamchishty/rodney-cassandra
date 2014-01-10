
CREATE TABLE users (
	id uuid,
  	time_uuid timeuuid,
  	name text,
  	surname text, 
  	joined_date timestamp,
  	age int,
  	reference_number bigint,
  	private_account boolean,
  	nicknames list<text>,
  	friends set<text>,
  	posts map<timestamp, text>,
  	primary_address text,
  	PRIMARY KEY (id,  time_uuid)
 );
 
INSERT INTO users 
	(id, time_uuid, name, surname, joined_date, age,
	reference_number, private_account, nicknames, 
	friends, posts, primary_address)
VALUES
	(7da3f8a7-3805-45c0-b7b2-212595b47e9f, a4a70900-24e1-11df-8924-001ff3591711,
	'sam', 'wise', '2013-10-11 12:45:00', 25, 2788378236743, true,
	['sammy', 'chubby'], {'frodo', 'gandalf', 'aragorn'},
	{'2013-10-11 12:45:00+0300': 'The path to helms deep.', '2013-11-26 16:11:23+0300': 'There and back.'}, 
	'{"house" : "25", "postcode" : "SW11AA"}');
	
	

	