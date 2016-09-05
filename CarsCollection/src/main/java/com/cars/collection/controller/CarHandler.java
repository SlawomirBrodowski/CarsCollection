package com.cars.collection.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;

import com.cars.collection.model.Car;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
/*
This Class is responsible for read/write object to database and filter objects

*/
public class CarHandler {
    private Grid grid=new Grid();
	private static CarHandler instance;
	private static final Logger LOGGER = Logger.getLogger(CarHandler.class.getName());

	private final HashMap<Long, Car> cars = new HashMap<>();
	private long nextId = 0;

	private CarHandler() {
	}
	

	public static CarHandler getInstance(){
		if (instance == null) {
			instance = new CarHandler();
			instance.loadDatabase();
			
		}
		return instance;
	}

	/**
	 * @return all available Car objects.
	 */
	public synchronized List<Car> findAll() {
		return findAll(null);
	}

	/**
	 * Finds all Car's that match given filter.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string
	 *            if all objects should be returned.
	 * @return list a Car objects
	 */
	public synchronized List<Car> findAll(String stringFilter) {
		ArrayList<Car> arrayList = new ArrayList<>();
		for (Car description : cars.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| description.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(description.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CarHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Car>() {

			@Override
			public int compare(Car o1, Car o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	/**
	 * Finds all Car's that match given filter and limits the resultset.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string
	 *            if all objects should be returned.
	 * @param start
	 *            the index of first result
	 * @param maxresults
	 *            maximum result count
	 * @return list a Car objects
	 */
	public synchronized List<Car> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<Car> arrayList = new ArrayList<>();
		for (Car car : cars.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| car.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(car.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CarHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Car>() {

			@Override
			public int compare(Car o1, Car o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}

	/**
	 * @return the amount of all cars in the system
	 */
	public synchronized long count() {
		return cars.size();
	}

	/**
	 * Deletes a car from a system
	 */
	public synchronized void delete(Car value) {
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		session.delete(value);
		session.getTransaction().commit();
		session.close();
		loadDatabase();
		cars.remove(value.getId());
	}

	/**
	 * Persists or updates car in the system. Also assigns an identifier
	 * for new Car instances.
	 *
	 * @param entry
	 */
	public synchronized void save(Car entry) {
		
if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Car is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
		
			Session session = HibernateUtil.getSessionfactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(entry);
			session.getTransaction().commit();
			session.close();
         
			
			
		
		
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		cars.put(entry.getId(), entry);
		
	}
	
	public void loadDatabase(){
		Session session=HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List result = session.createQuery("from Car").list();
		for (Car item : (List<Car>) result) {
		cars.put(item.getId(), item);
	
		
		
		}
		session.getTransaction().commit();
		session.close();
		
	}


}