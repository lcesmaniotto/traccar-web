package org.traccar.web.client.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.traccar.web.client.model.DeviceProperties;
import org.traccar.web.shared.model.Device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

public class DeviceView extends Composite implements SelectionChangedEvent.SelectionChangedHandler<Device> {

    private static DeviceViewUiBinder uiBinder = GWT.create(DeviceViewUiBinder.class);

    interface DeviceViewUiBinder extends UiBinder<Widget, DeviceView> {
    }

    public interface DeviceHandler {
        public void onSelected(Device device);
        public void onAdd();
        public void onEdit(Device device);
        public void onRemove(Device device);
    }

    private DeviceHandler deviceHandler;

    @UiField
    ContentPanel contentPanel;

    public ContentPanel getView() {
        return contentPanel;
    }

    @UiField
    TextButton addButton;

    @UiField
    TextButton editButton;

    @UiField
    TextButton removeButton;

    @UiField(provided = true)
    ColumnModel<Device> columnModel;

    @UiField(provided = true)
    ListStore<Device> store;

    @UiField
    Grid<Device> grid;

    public DeviceView(DeviceHandler deviceHandler) {
        this.deviceHandler = deviceHandler;

        DeviceProperties deviceProperties = GWT.create(DeviceProperties.class);

        List<ColumnConfig<Device, ?>> columnConfigList = new LinkedList<ColumnConfig<Device, ?>>();
        columnConfigList.add(new ColumnConfig<Device, String>(deviceProperties.name(), 0, "Name"));
        columnConfigList.add(new ColumnConfig<Device, String>(deviceProperties.uniqueId(), 0, "Unique Identifier"));
        columnModel = new ColumnModel<Device>(columnConfigList);

        store = new ListStore<Device>(deviceProperties.id());

        uiBinder.createAndBindUi(this);

        grid.getSelectionModel().addSelectionChangedHandler(this);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Device> event) {
        editButton.setEnabled(!event.getSelection().isEmpty());
        removeButton.setEnabled(!event.getSelection().isEmpty());

        if (event.getSelection().isEmpty()) {
            deviceHandler.onSelected(null);
        } else {
            deviceHandler.onSelected(event.getSelection().get(0));
        }
    }

    public void load(Collection<Device> devices) {
        store.addAll(devices);
    }

    @UiHandler("addButton")
    public void onAddClicked(SelectEvent event) {
        deviceHandler.onAdd();
    }

    public void add(Device device) {
        store.add(store.size(), device);
    }

    @UiHandler("editButton")
    public void onEditClicked(SelectEvent event) {
        deviceHandler.onEdit(grid.getSelectionModel().getSelectedItem());
    }

    public void update(Device device) {
        store.update(device);
    }

    @UiHandler("removeButton")
    public void onRemoveClicked(SelectEvent event) {
        deviceHandler.onRemove(grid.getSelectionModel().getSelectedItem());
    }

    public void remove(Device device) {
        store.remove(device);
    }

}