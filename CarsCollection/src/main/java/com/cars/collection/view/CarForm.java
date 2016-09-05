package com.cars.collection.view;



import com.cars.collection.controller.CarHandler;
import com.cars.collection.model.Car;
import com.cars.collection.model.CarStatus;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;

public class CarForm extends CarFormDesign {
	
	CarHandler service = CarHandler.getInstance();
	private Car car;
	private MyUI myUI;

	public CarForm(MyUI myUI) {
		this.myUI = myUI;
		status.addItems(CarStatus.values());
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());

	}

	public void setCar(Car car) {
		this.car = car;
		BeanFieldGroup.bindFieldsUnbuffered(car,this);

		// show delete buttons only for customers in database

		delete.setVisible(car.isPersisted());
		setVisible(true);
		make.selectAll();
	}

	private void delete() {
		service.delete(car);
		myUI.updateList();
		setVisible(false);
	}

	private void save() {
		service.save(car);
		myUI.updateList();
		setVisible(false);
	}
}
