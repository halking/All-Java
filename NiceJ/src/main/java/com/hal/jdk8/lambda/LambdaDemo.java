package com.hal.jdk8.lambda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hal.entity.Person;
import com.hal.entity.Person.Sex;
import com.hal.utils.LogUtil;

public class LambdaDemo {
	private static final Logger logger = LogUtil.initLog(LambdaDemo.class);
	public static void main(String[] args) {
		List<Person> roster = new ArrayList<Person>();
		Person p1 = new Person("huang",LocalDate.now(), Sex.MALE, "112@qq.com", 21);
		Person p2 = new Person("zhang",LocalDate.now(), Sex.FEMALE, "112@qq.com", 21);
		Person p3 = new Person("wang",LocalDate.now(), Sex.MALE, "112@qq.com", 29);
		roster.add(p1);
		roster.add(p2);
		roster.add(p3);
		class CheckPersonEligibleForSelectiveService implements CheckPerson {
			public boolean test(Person p) {
				return p.gender == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			}
		}
		logger.info(" \nimplement class test: ");
		printPerson(roster, new CheckPersonEligibleForSelectiveService());
		
		logger.info(" \nAnonymous class test: ");
		printPerson(roster, new CheckPerson() {
			public boolean test(Person p) {
				return p.gender == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			}
		});
		logger.info(" \nlanbda expression  test:");
		printPerson(roster,    (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25);
		logger.info(" \nFunction class test:");
		printPersonWithFunction(roster, (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25);
		logger.info(" \nFunction class test: ");
		processPerson(roster,  (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25, p -> p.printPerson());
		
		logger.info(" \nFunction class test: ");
		processPersonsWithFunction(roster,  (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25, p -> p.getEmailAddress(),email ->System.out.println("email:"+email));
		
		System.out.println(" \n=================lambda express aggregation operation===============");
		roster.stream()
		.filter( p -> p.gender==Person.Sex.MALE &&  p.getAge()>=18 && p.getAge()<=25)
		.map(p->p.getEmailAddress())
		.forEach(email->System.out.println(email));
	}

	interface CheckPerson {
		boolean test(Person p);
	}

	public static void printPerson(List<Person> list, CheckPerson test) {
		for (Person person : list) {
			if (test.test(person)) {
				person.printPerson();
			}
		}
	}

	public static void printPesonsOlderThan(List<Person> list, int age) {
		for (Person person : list) {
			if (person.getAge() == age) {
				person.printPerson();
			}
		}
	}

	public static void printPersonWithFunction(List<Person> list, Predicate<Person> predicate) {
		for (Person person : list) {
			if (predicate.test(person)) {
				person.printPerson();
			}
		}
	}

	public static void processPerson(List<Person> list, Predicate<Person> predicate, Consumer<Person> block) {
		for (Person person : list) {
			if (predicate.test(person)) {
				block.accept(person);
			}
		}
	}

	public static void processPersonsWithFunction(List<Person> roster, Predicate<Person> tester, Function<Person, String> mapper,
			Consumer<String> block) {
		for (Person person : roster) {
			if (tester.test(person)) {
				String data = mapper.apply(person);
				block.accept(data);
			}
		}
	}
}
