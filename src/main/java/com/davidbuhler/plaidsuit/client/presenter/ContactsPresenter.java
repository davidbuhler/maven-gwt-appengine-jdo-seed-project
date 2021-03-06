package com.davidbuhler.plaidsuit.client.presenter;

import com.davidbuhler.plaidsuit.client.ContactsServiceAsync;
import com.davidbuhler.plaidsuit.client.event.AddContactEvent;
import com.davidbuhler.plaidsuit.client.view.ContactsView;
import com.davidbuhler.plaidsuit.shared.constants.UserMessages;
import com.davidbuhler.plaidsuit.shared.dto.ContactDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.List;
import java.util.logging.Logger;

public class ContactsPresenter implements Presenter, ContactsView.Presenter<ContactDTO>
{

	private static final Logger LOG = Logger.getLogger(ContactsPresenter.class.getName());
	private final ContactsServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final ContactsView<ContactDTO> view;

	public ContactsPresenter(ContactsServiceAsync rpcService, HandlerManager eventBus, ContactsView<ContactDTO> view)
	{
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);

	}

	public void onAddButtonClicked()
	{
		eventBus.fireEvent(new AddContactEvent());
	}

	@Override
	public void onRefreshButtonClicked()
	{
		fetchContacts();
	}

	public void onDeleteButtonClicked()
	{
	}

	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(view.asWidget());
		fetchContacts();
	}

	private void fetchContacts()
	{
		rpcService.listContacts(new AsyncCallback<List<ContactDTO>>()
		{
			public void onFailure(Throwable caught)
			{
				LOG.severe(caught.toString());
				Window.alert(UserMessages.ERROR_FETCHING_CONTACTS);
			}

			public void onSuccess(List<ContactDTO> result)
			{
				view.setRowData(result);
			}
		});
	}
}
