import urllib
import urllib2
import string
import re
from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from sqlalchemy.orm import sessionmaker
from sqlalchemy import *
from BeautifulSoup import BeautifulSoup

db = SQLAlchemy

engine = create_engine("mysql+mysqldb://projectuser:projectmysqlUser@localhost:3306/project")
connection = engine.connect()
Session = sessionmaker(bind = engine)
session = Session()

def convert_time_to_timestamp_format(timeam):
	hour = ''
	minute = ''
	foundHour = False
	pm = False#whether or not the time was am/pm

	#parsing the date object to turn it into a mysql friendly format
	#I am making sure that hours with one digit are accounted for
	#as well as converting the time into a mysql format of HH:MM:SS
	for char in timeam:
		if char != ':' and foundHour == False:
			hour += char
		elif char == ':':
			foundHour = True#the hour part of the time has been found
		elif char != 'a' and char != 'p' and char != 'm':
			minute += char
		elif char == 'p':
			pm = True
	if pm == True:
		#without the check for 12 it will become 24:**:** and that's bad
		if int(hour) != 12:
			hour = str(int(hour) + 12)

	return hour+":"+minute+":00"

def process_time(time):
	print time
	
	
def insert_into_database(event_name,date,start_time,end_time):
	print event_name,date,start_time,end_time
	results = session.query("event_name").from_statement("select event_name from event where event_name = :ename").\
		params(ename = event_name).all()

	if len(results) == 0:#event hasn't been added)
		connection.execute("""insert into event (event_name,date,start_time,end_time) values (%s,%s,%s,%s);""",event_name,str(date),start_time,end_time)
	print results
	
def process_date(date):
	""" Will process the date from the scraped format to mysql format"""
	month = date[:date.find('-')]
	date = date[date.find('-')+1:]
	day = date[:date.find('-')]
	date = date[date.find('-')+1:]
	year = date
	return year+'-'+month+'-'+day


user_agent = "Ring ring ring ring ring ring ring BANANA PHONE"
headers = {"user-Agent:":user_agent}#setting user agent in request

#opening the request with the specified headers
request = urllib2.Request("http://www.coa.gatech.edu/news/events/eventcal",headers = headers)
response = urllib2.urlopen(request)

#reading the response and setting up BeautifulSoup
the_page = response.read()
soup = 	BeautifulSoup(the_page)
soup.originalEncoding

results = soup.findAll("tr",{'id':re.compile('id-[1-9]-eventcalendar')})

for result in results:
	#just finding the name and converting to utf8
	name = str(result.find('a').contents)#name is in the a tag
	name = name.encode("ascii")

	#finding the date
	date_stuff = result.find('span',{'class':'date-display-single'})
	date_stuff = str(date_stuff.contents)

	#because there are more things than just the name of the event in the contents
	date = date_stuff[3:date_stuff.find(',',15)]
	start_date_stuff = ''
	try:
		start_date_stuff = result.find('span',{'class':'date-display-start'}).contents
	except:
		#honeybadger don't give no fucks
		pass

	start_date = ''
	#do some error checking here.
	if start_date_stuff != '':
		start_date = start_date_stuff[0]

	try:
		end_date_stuff = result.find('span',{'class':'date-display-end'}).contents
	except:
		pass
	print end_date_stuff#it's a list
