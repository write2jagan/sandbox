/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.windsource.osgi.crud;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.osgi.framework.BundleContext;
import org.windsource.osgi.crud.model.Incident;
import org.windsource.osgi.crud.service.IncidentService;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	private static final transient Log LOG = LogFactory.getLog(HomePage.class);

	@SpringBean
	private IncidentService incidentService;

	@SpringBean
	private BundleContext blueprintBundleContext;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public HomePage(final PageParameters parameters) {

		LOG.debug("Spring service : " + incidentService.toString());
		// Add the simplest type of label

		final WebMarkupContainer incidentListContainer = new WebMarkupContainer(
				"incidentListContainer");
		final WebMarkupContainer incidentDetailsContainer = new WebMarkupContainer(
				"incidentDetailsContainer");
		final Form incidentForm = new Form("incidentForm", incidentModel);

		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		ListView<Incident> incidentListView = new PropertyListView<Incident>(
				"incidentListView", incidentListModel) {

			@Override
			protected void populateItem(ListItem<Incident> item) {
				final Incident incident = item.getModelObject();
				item.add(new Label("incidentDate", String.valueOf(incident
						.getIncidentDate())));
				item.add(new Label("incidentRef", incident.getIncidentRef()));
				item.add(new Label("givenName", incident.getGivenName()));
				item.add(new Label("familyName", incident.getFamilyName()));
				item.add(new Label("summary", incident.getSummary()));
				item.add(new Label("details", incident.getDetails()));
				item.add(new Label("email", incident.getEmail()));
				item.add(new Label("phone", incident.getPhone()));
				item.add(new Label("creationUser", incident.getCreationUser()));
				item.add(new Label("creationDate", String.valueOf(incident
						.getCreationDate())));

				item.add(new AjaxFallbackButton("editIncident", incidentForm) {

					@Override
					protected void onSubmit(AjaxRequestTarget target,
							Form<?> form) {
						incidentForm.setModelObject(incident);
						target.addComponent(incidentDetailsContainer);
					}
				}.setDefaultFormProcessing(false));

				item.add(new AjaxFallbackButton("deletePerson", incidentForm) {

					@Override
					protected void onSubmit(AjaxRequestTarget target,
							Form<?> form) {
						try {
							incidentService.removeIncident(incident
									.getIncidentId());
							// personForm.setModelObject(new Person());
						} catch (Exception ex) {
							error("Could not delete person");
						}
						target.addComponent(incidentListContainer);
						target.addComponent(incidentDetailsContainer);
						target.addComponent(feedbackPanel);
					}
				}.setDefaultFormProcessing(false));

			}

		};

		incidentDetailsContainer.add(new RequiredTextField("incidentDate"));
		incidentDetailsContainer.add(new RequiredTextField("incidentRef"));
		incidentDetailsContainer.add(new RequiredTextField("givenName"));
		incidentDetailsContainer.add(new RequiredTextField("familyName"));
		incidentDetailsContainer.add(new RequiredTextField("summary"));
		incidentDetailsContainer.add(new RequiredTextField("details"));
		incidentDetailsContainer.add(new RequiredTextField("email"));
		incidentDetailsContainer.add(new RequiredTextField("phone"));
		incidentDetailsContainer.add(new RequiredTextField("creationUser"));
		incidentDetailsContainer.add(new RequiredTextField("creationDate"));
		incidentDetailsContainer.add(new AjaxFallbackButton("save",
				incidentForm) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					Incident incident = incidentModel.getObject();
					if (incident.getIncidentId() != null) {
						incidentService.getIncident(incident.getIncidentId());
					} else {
						incidentService.saveIncident(incidentModel.getObject());
					}
					incidentModel.setObject(new Incident());
					target.addComponent(incidentListContainer);
					target.addComponent(feedbackPanel);
				} catch (Exception ex) {
					error("Could not save person");
				}
				target.addComponent(incidentListContainer);
				target.addComponent(incidentListContainer);
				target.addComponent(feedbackPanel);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}

		});

		incidentDetailsContainer.add(new AjaxFallbackButton("cancel",
				incidentForm) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Incident person = new Incident();
				incidentForm.setModelObject(person);
				target.addComponent(incidentDetailsContainer);
				target.addComponent(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
		}.setDefaultFormProcessing(false));

		incidentListContainer.setOutputMarkupId(true);
		incidentDetailsContainer.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupId(true);

		add(incidentForm);
		incidentListContainer.add(incidentListView);
		incidentForm.add(incidentListContainer);
		incidentForm.add(incidentDetailsContainer);
		incidentForm.add(feedbackPanel);
	}

	// model

	final CompoundPropertyModel<Incident> incidentModel = new CompoundPropertyModel<Incident>(
			new Incident());

	final IModel<List<Incident>> incidentListModel = new LoadableDetachableModel<List<Incident>>() {

		@Override
		protected List<Incident> load() {

			return incidentService.findIncident();
		}

	};

}
