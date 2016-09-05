package com.cars.collection.view;

import java.text.DateFormat;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;















import com.cars.collection.controller.CarHandler;
import com.cars.collection.model.Car;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.themes.ValoTheme;
/**Author Krzysztof Leśniak <inwokacja@gmail.com>
/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("com.cars.collection.MyAppWidgetset")
public class MyUI extends UI {

	private CarHandler carshandler = CarHandler.getInstance();
	private Grid grid = new Grid();
	private TextField filterText = new TextField();
	private CarForm form = new CarForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
	
		final VerticalLayout layout = new VerticalLayout();
		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setSpacing(true);
		main.setSizeFull();
		
		grid.setSizeFull();
		main.setExpandRatio(grid, 1);
		
		form.setVisible(false);
		
		final Label label = new Label();
		label.setValue("Ewidencja pojazdów komisu samochodowego Kris-Auto ");
		label.addStyleName("h1");
		

		filterText.setInputPrompt("Filtrowanie po nazwie...");
		filterText.addTextChangeListener(e -> {
			grid.setContainerDataSource(new BeanItemContainer<>(Car.class,
					carshandler.findAll(e.getText())));
		});

		Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Wyczyść filtrowanie");
		clearFilterTextBtn.addClickListener(e -> {
			filterText.clear();
			updateList();
		});
		
		Button addCustomerBtn = new Button("Dodaj pojazd");
		addCustomerBtn.addClickListener(e -> {
		    grid.select(null);
		    form.setCar(new Car());
		}); 
		
		
		
	    CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		filtering.setHeight("60px");
		
		HorizontalLayout toolbar = new HorizontalLayout(addCustomerBtn);
		toolbar.setSpacing(true);
		
        grid.setColumns("make", "model", "checkInDate", "status");
   
                                               
        updateGridHeaders();
     
		
		
		layout.addComponents(label, toolbar, main);
		updateList();
		layout.setMargin(true);

	
		
		grid.addSelectionListener(event -> {
		    if (event.getSelected().isEmpty()) {
		        form.setVisible(false);
		    } else {
		        Car car = (Car) event.getSelected().iterator().next();
		        form.setCar(car);
		    }
		});
		
		setContent(layout);
	}

	public void updateList() {
		
		List<Car> customers = carshandler.findAll(filterText.getValue());
		grid.setContainerDataSource(new BeanItemContainer<>(Car.class,
				customers));
	}
//This method add aliases to grid columns
	 public void updateGridHeaders(){
	        grid.getColumn("make").setHeaderCaption("Marka");
	        grid.getColumn("model").setHeaderCaption("Model");
	        grid.getColumn("checkInDate").setHeaderCaption("Data przyjęcia");
	        
	        }
	 
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
