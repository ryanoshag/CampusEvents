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

def process_time(times):
	times = times[2:]
	start_time = times[:times.find('to')]
	end_time = times[times.find('to')+2:]
	start_time = convert_time_to_timestamp_format(start_time)
	end_time = convert_time_to_timestamp_format(end_time)
	return start_time, end_time
	
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




user_agent = "Mozilla/5 (Solaris 10) Gecko"
headers = {"user-Agent:":user_agent}#setting user agent in request

#opening the request with the specified headers
request = urllib2.Request("http://www.gatech.edu/calendar/upcoming",headers = headers)
response = urllib2.urlopen(request)

#reading the response and setting up BeautifulSoup
the_page = response.read()
soup = 	BeautifulSoup(the_page)

results = soup.findAll("div",{'class':re.compile('views-row')})
for result in results:
	#result encapsulates a div of all of the attributes we need 
	#to create a database entry for that particular event
	
	#the name that is required is in a link tag inside of h4 with no 
	#id nor class so more processing has to be done and this was easiest
	#way to guarantee accuracy
	nameformat = result.find('h4')
	event_name = nameformat.find('a').string

	#going to need to do some processing to extract the name from the inner tag
	time_object = result.find('span',{'class':'date-display-single'})
	#print dir(time_object)
	date = ''
	times = ''
	start_time = ''
	end_time = ''
	try:
		#going to extract the text from the time object and do some processing
		date = time_object.getText()
		date = str(date)#unicode to string
		date = date[5:]#getting rid of the day, and a space
		date = date.replace('/','-')#formatting date for mysql insertion
		times = date[date.find(' '):]#start and end times are after the date
		start_time,end_time = process_time(times)
		date = date[:date.find(' ')]#the date part required is before the space
		date = process_date(date)

	except AttributeError:
		pass#I really don't want to do anything here

	location = result.findAll('p',{'class':'event-location'})
	insert_into_database(event_name,date,start_time,end_time)