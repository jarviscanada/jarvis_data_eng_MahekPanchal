### Q1: The club is adding a new facility - a spa. We need to add it into the facilities table. Use the following values: facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
Solution: insert into cd.facilities (facid,name,membercost,guestcost,initialoutlay,monthlymaintenance) VALUES (9,'Spa',20,30,100000,800);

### Q2: Let's try adding the spa to the facilities table again. This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant. Use the following values for everything else:Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
Solution: insert into cd.facilities(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) select (select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

### Q3: We made a mistake when entering the data for the second tennis court. The initial outlay was 10000 rather than 8000: you need to alter the data to fix the error.
Solution: update cd.facilities set initialoutlay=10000 where facid=1;

### Q4: We want to alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values for the prices, so that we can reuse the statement if we want to.
Solution: update cd.facilities facs set membercost = facs2.membercost * 1.1,guestcost = facs2.guestcost * 1.1 from (select * from cd.facilities where facid = 0) facs2 where facs.facid = 1;

### Q5: As part of a clearout of our database, we want to delete all bookings from the cd.bookings table. How can we accomplish this? 
Solution: delete from cd.bookings;

### Q6: We want to remove member 37, who has never made a booking, from our database. How can we achieve that?
Solution: delete from cd.members where memid=37;

### Q7: How can you produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost? Return the facid, facility name, member cost, and monthly maintenance of the facilities in question.
Solution: select facid, name, membercost, monthlymaintenance from cd.facilities where 
membercost > 0 and (membercost < monthlymaintenance/50.0);

### Q8: How can you produce a list of all facilities with the word 'Tennis' in their name?
Solution: select * from cd.facilities where name like '%Tennis%';

### Q9: How can you retrieve the details of facilities with ID 1 and 5? Try to do it without using the OR operator.
Solution: select * from cd.facilities where facid in (1,5);

### Q10: How can you produce a list of members who joined after the start of September 2012? Return the memid, surname, firstname, and joindate of the members in question.
Solution: select memid, surname, firstname, joindate from cd.members where joindate >= '2012-09-01';

### Q11: You, for some reason, want a combined list of all surnames and all facility names. Yes, this is a contrived example :-). Produce that list!
Solution: SELECT surname FROM cd.members UNION SELECT name FROM cd.facilities;

### Q12: How can you produce a list of the start times for bookings by members named 'David Farrell'?
Solution: select bks.starttime from cd.bookings bks inner join cd.members mems on mems.memid = bks.memid where mems.firstname='David' and mems.surname='Farrell';

### Q13: How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.
Solution: select time.starttime, fac.name from cd.bookings as time left join 
cd.facilities as fac on time.facid=fac.facid where DATE(time.starttime)='2012-09-21' and fac.name like '%Tennis Court%' order by time.starttime;

### Q14: How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).
Solution:select mems.firstname as fname, mems.surname as sname, recs.firstname as rfname, recs.surname as rsname from cd.members mems left outer join cd.members recs on 
recs.memid = mems.recommendedby order by sname, fname;

### Q15: How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).
Solution: select distinct recs.firstname as rfname, recs.surname as rsname from 
cd.members mems left outer join cd.members recs on recs.memid = mems.recommendedby order by rsname, rfname;

### Q16: How can you output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.
Solution:select distinct member.firstname || ' ' || member.surname as member ,
(select ref.firstname || ' ' || ref.surname as recommender from cd.members as ref
where ref.memid=member.recommendedby)
from cd.members member order by member;

### Q17: Produce a count of the number of recommendations each member has made. Order by member ID.
Solution: select recommendedby,count(*) from cd.members as mem where recommendedby is 
not null group by mem.recommendedby order by recommendedby; 

### Q18: Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.
Solution: select distinct fac.facid,sum(book.slots) from cd.bookings as book left join 
cd.facilities as fac on book.facid = fac.facid group by fac.facid order by facid;

### Q19: Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.
Solution: select book.facid,sum(slots) as total from cd.bookings as book left join 
cd.facilities as fac on book.facid=fac.facid  where starttime >= '2012-09-01' and 
starttime <'2012-10-01'group by book.facid order by sum(slots); 

### Q20: Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.
Solution: select facid, extract(month from starttime) as month, sum(slots) as "Total Slots" from cd.bookings where extract(year from starttime) = 2012 group by facid, month
order by facid, month;

### Q21: Find the total number of members (including guests) who have made at least one booking.
Solution: select count(distinct memid) from cd.bookings

### Q22: Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.
Solution: select mem.surname,mem.firstname,mem.memid,min(book.starttime) from cd.bookings as book inner join cd.members as mem on mem.memid=book.memid where starttime>='2012-09-01' group by mem.surname,mem.firstname,mem.memid order by memid;

### Q23: Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.
Solution: select (select count(*) from cd.members) as count, firstname, surname
from cd.members order by joindate;

### Q24: Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.
Solution: select row_number() over(order by joindate), firstname, surname from 
cd.members order by joindate;

### Q25: Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tieing results get output.
Solution: select facid, total from ( select facid, sum(slots) total, rank() over (order by sum(slots) desc) rank from cd.bookings group by facid ) as ranked where rank = 1;

### Q26: Output the names of all members, formatted as 'Surname, Firstname'
Solution: select surname || ', ' || firstname as name from cd.members;

### Q27: You've noticed that the club's member table has telephone numbers with very inconsistent formatting. You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.
Solution: select memid, telephone from cd.members where telephone ~ '[()]';

### Q28: You'd like to produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.
Solution: select substr (mems.surname,1,1) as letter, count(*) as count from cd.members mems group by letter order by letter;

