package app.todo.views.bienvenidoatodolistapp;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Bienvenido a To Do List App !")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.HOME_SOLID)
public class BienvenidoaToDoListAppView extends Composite<VerticalLayout> {

    public BienvenidoaToDoListAppView() {
        H1 h1 = new H1();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h1.setText("Ingresa a unas de las pestañas de la izquierda para comenzar a gestionar tus tareas");
        h1.setWidth("max-content");
        getContent().add(h1);
    }
}
