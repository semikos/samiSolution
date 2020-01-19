package com.tcs.bfss.interview;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tcs.bfss.interview.model.Book;
import com.tcs.bfss.interview.utils.GivenDatas;

public class BooksTests {
	private static List<Book> inputBooks;
	private static List<String> distinctCountries;
	private static List<String> orderedAuthorNameIfPriceEqual;
	private static List<Integer> orderedPriceBooks;

	@BeforeAll
	public static void initData() {
		inputBooks = GivenDatas.givenBooks();
		distinctCountries = Arrays.asList("United Kinkdom", "United States", "France", "Germany");
		orderedAuthorNameIfPriceEqual = Arrays.asList("Bob LeVitus", "Charlton Miner Lewis", "Gwen Shamblin",
				"Marion Nestle");
		orderedPriceBooks = Arrays.asList(54, 32, 14, 10, 10, 10, 10, 8, 5);
	}

	/*
	 * Questions :
	 * 
	 * Create two methods for the point 1 and 2 bellow this paragraph using Java 8
	 * Streams Api. The methods cas be created in this class test to avoid wasting
	 * time. Create a unit tests for your methods as you do usually using Junit. You
	 * can use a data examples from com.tcs.bfss.interview.utils.GivenDatas if you
	 * want it.
	 * 
	 * 1- Method taking a given list of books and return new list of books -
	 * filtered by years > 1990 - sorted by price (Most expensive on top) - if the
	 * price is equal then sort by Author name
	 * 
	 * 2- Method taking list of books and return list of countries names of authors
	 * without duplicates
	 * 
	 */
	private List<Book> filterBooks(List<Book> books) {
		return books.stream().filter(b -> b.getDate().getYear() > 1990).sorted(Comparator.comparing(Book::getPrice)
				.reversed().thenComparing(Comparator.comparing(Book::getAuthor, (author1, author2) -> {
					return author1.getName().compareTo(author2.getName());
				}))).collect(Collectors.toList());
	}

	private List<String> findBooksCountries(List<Book> books) {
		return books.stream().map(b -> b.getAuthor().getCountry().getName()).distinct().collect(Collectors.toList());
	}

	@Test
	public void testFilterBooksUsingYearCriteria() {

		List<Book> filteredBooks = filterBooks(inputBooks);
		Optional<Book> book = filteredBooks.stream().filter(b -> b.getDate().getYear() <= 1990).findAny();
		Assert.assertFalse(book.isPresent());
		Optional<Book> book2 = filteredBooks.stream().filter(b -> b.getDate().getYear() > 1990).findAny();
		Assert.assertTrue(book2.isPresent());
	}

	@Test
	public void testFilterBooksIsOrdered() {
		List<Book> filteredBooks = filterBooks(inputBooks);
		List<Integer> filteredBooksPrice = filteredBooks.stream().map(book -> book.getPrice())
				.collect(Collectors.toList());
		Assert.assertEquals(filteredBooksPrice, orderedPriceBooks);
	}

	@Test
	public void testFilterBooksByAuthorIfPriceIsEqual() {
		List<Book> filteredBooks = filterBooks(inputBooks);
		List<String> authorNamesIfPriceIsEqualTo10 = filteredBooks.stream().filter(book -> book.getPrice() == 10)
				.map(book -> book.getAuthor().getName()).collect(Collectors.toList());
		Assert.assertEquals(authorNamesIfPriceIsEqualTo10, orderedAuthorNameIfPriceEqual);
	}

	@Test
	public void testFindBooksCountries() {
		List<String> booksCountries = findBooksCountries(inputBooks);
		Assert.assertEquals(booksCountries, distinctCountries);
	}

}
