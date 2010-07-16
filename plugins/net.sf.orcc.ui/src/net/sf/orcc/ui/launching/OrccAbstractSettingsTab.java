/*
 * Copyright (c) 2010, IETR/INSA of Rennes
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the IETR/INSA of Rennes nor the names of its
 *     contributors may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package net.sf.orcc.ui.launching;

import static net.sf.orcc.OrccLaunchConstants.ACTIVATE_BACKEND;
import static net.sf.orcc.OrccLaunchConstants.BACKEND;
import static net.sf.orcc.OrccLaunchConstants.DEFAULT_BACKEND;
import static net.sf.orcc.OrccLaunchConstants.OUTPUT_FOLDER;
import static net.sf.orcc.OrccLaunchConstants.PROJECT;
import static net.sf.orcc.OrccLaunchConstants.SIMULATOR;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.orcc.ui.OrccActivator;
import net.sf.orcc.ui.launching.impl.OptionWidgetManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public abstract class OrccAbstractSettingsTab extends
		AbstractLaunchConfigurationTab implements ModifyListener {

	protected Combo comboPlugin;

	protected Group groupOptions;

	protected Map<String, List<OptionWidget>> optionWidgets;

	protected String plugin;

	protected Text textOutput;

	private Text textProject;

	protected boolean updateLaunchConfiguration;

	protected void browseOutputFolder(Shell shell) {
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NONE);
		dialog.setMessage("Select output folder:");
		if (getFolderFromText()) {
			// set initial directory if it is valid
			dialog.setFilterPath(textOutput.getText());
		}

		String dir = dialog.open();
		if (dir != null) {
			textOutput.setText(dir);
		}
	}

	private void browseProject(Shell shell) {
		ElementTreeSelectionDialog tree = new ElementTreeSelectionDialog(shell,
				WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		tree.setAllowMultiple(false);
		tree.setInput(ResourcesPlugin.getWorkspace().getRoot());

		IProject project = getProjectFromText();
		if (project != null) {
			tree.setInitialSelection(project);
		}

		tree.setMessage("Please select an existing project:");
		tree.setTitle("Choose an existing project");

		tree.setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				if (selection.length == 1) {
					if (selection[0] instanceof IProject) {
						return new Status(IStatus.OK, OrccActivator.PLUGIN_ID,
								"");
					} else {
						return new Status(IStatus.ERROR,
								OrccActivator.PLUGIN_ID,
								"Only projects can be selected");
					}
				}

				return new Status(IStatus.ERROR, OrccActivator.PLUGIN_ID,
						"No project selected.");
			}

		});

		// opens the dialog
		if (tree.open() == Window.OK) {
			project = (IProject) tree.getFirstResult();
			textProject.setText(project.getName());
		}
	}

	@Override
	public void createControl(Composite parent) {
		Font font = parent.getFont();

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(font);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(data);
		setControl(composite);

		createControlProject(font, composite);
		createControlPlugin(font, composite);
		createControlOption(font, composite);
		createOptions();
	}

	private void createControlBrowseProject(Font font, final Group group) {
		textProject = new Text(group, SWT.BORDER | SWT.SINGLE);
		textProject.setFont(font);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textProject.setLayoutData(data);
		textProject.addModifyListener(this);

		Button buttonBrowse = new Button(group, SWT.PUSH);
		buttonBrowse.setFont(font);
		data = new GridData(SWT.FILL, SWT.CENTER, false, false);
		buttonBrowse.setLayoutData(data);
		buttonBrowse.setText("&Browse...");
		buttonBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseProject(group.getShell());
			}
		});
	}

	protected void createControlOption(Font font, Composite parent) {
		groupOptions = new Group(parent, SWT.NONE);
		groupOptions.setFont(font);
		groupOptions.setText("&Options:");

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		groupOptions.setLayout(layout);

		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		groupOptions.setLayoutData(data);
	}

	protected void createControlOutputFolder(Font font, final Group group) {
		Label lbl = new Label(group, SWT.NONE);
		lbl.setFont(font);
		lbl.setText("Output folder:");
		GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lbl.setLayoutData(data);

		textOutput = new Text(group, SWT.BORDER | SWT.SINGLE);
		textOutput.setFont(font);
		data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textOutput.setLayoutData(data);
		textOutput.addModifyListener(this);

		Button buttonBrowse = new Button(group, SWT.PUSH);
		buttonBrowse.setFont(font);
		data = new GridData(SWT.FILL, SWT.CENTER, false, false);
		buttonBrowse.setLayoutData(data);
		buttonBrowse.setText("&Browse...");
		buttonBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseOutputFolder(group.getShell());
			}
		});
	}

	abstract protected void createControlPlugin(Font font, Composite parent);

	private void createControlProject(Font font, Composite parent) {
		final Group group = new Group(parent, SWT.NONE);
		group.setFont(font);
		group.setText("&Project:");
		group.setLayout(new GridLayout(2, false));
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		group.setLayoutData(data);

		createControlBrowseProject(font, group);
	}

	/**
	 * Creates the controls for all options, but do not show them yet.
	 */
	abstract protected void createOptions();

	@Override
	public void dispose() {
		optionWidgets.clear();
		if (groupOptions != null) {
			groupOptions.dispose();
		}
	}

	protected boolean getFolderFromText() {
		String value = textOutput.getText();
		File file = new File(value);
		if (file.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return null;
	}

	/**
	 * Get the current plugin type (BACKEND in compilation mode, SIMULATOR in
	 * simulation mode)
	 * 
	 * @return Correct OrccLaunchConstant plugin type identifier according to
	 *         current mode (compilation or simulation)
	 */
	public String getPluginType() {
		// By default, plugin is a backend
		return BACKEND;
	}

	private IProject getProjectFromText() {
		String value = textProject.getText();
		if (value.isEmpty()) {
			return null;
		}

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProject(value);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			// hide options of the previously-selected back-end (if any)
			if (plugin != null && !plugin.isEmpty()) {
				List<OptionWidget> widgets = optionWidgets.get(plugin);
				OptionWidgetManager.hideOptions(widgets);
			}

			plugin = configuration.getAttribute(getPluginType(), "");

			int index = comboPlugin.indexOf(plugin);
			if (index == -1) {
				comboPlugin.deselectAll();
			} else {
				comboPlugin.select(index);
			}

			updateLaunchConfiguration = false;
			String value = configuration.getAttribute(PROJECT, "");
			textProject.setText(value);

			value = configuration.getAttribute(OUTPUT_FOLDER, "");
			textOutput.setText(value);
			updateLaunchConfiguration = true;

			// initialize from all options
			for (Entry<String, List<OptionWidget>> entry : optionWidgets
					.entrySet()) {
				List<OptionWidget> widgets = entry.getValue();
				OptionWidgetManager.initializeFromOptions(widgets,
						configuration);
			}

			// show options of the newly-selected back-end or simulator plugin
			if (plugin != null && !plugin.isEmpty()) {
				List<OptionWidget> widgets = optionWidgets.get(plugin);
				OptionWidgetManager.showOptions(widgets);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			updateLaunchConfiguration = true;
		}
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		String value = textProject.getText();
		if (value.isEmpty()) {
			setErrorMessage("Project not specified");
			return false;
		}

		IProject project = getProjectFromText();
		if (project == null || !project.exists()) {
			setErrorMessage("Specified project does not exist");
			return false;
		}

		value = textOutput.getText();
		if (value.isEmpty()) {
			setErrorMessage("Output path not specified");
			return false;
		}

		if (!getFolderFromText()) {
			setErrorMessage("Given output path does not specify an existing folder");
			return false;
		}

		List<OptionWidget> widgets = optionWidgets.get(plugin);
		if (OptionWidgetManager.isValidOptions(widgets, launchConfig)) {
			setErrorMessage(null);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (updateLaunchConfiguration) {
			updateLaunchConfigurationDialog();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String value = textProject.getText();
		configuration.setAttribute(PROJECT, value);

		value = textOutput.getText();
		configuration.setAttribute(OUTPUT_FOLDER, value);

		int index = comboPlugin.getSelectionIndex();
		if (index != -1) {
			value = comboPlugin.getItem(index);
			// It may be either a backend or a simulator
			configuration.setAttribute(BACKEND, value);
			configuration.setAttribute(SIMULATOR, value);
		}

		List<OptionWidget> widgets = optionWidgets.get(plugin);
		if (widgets != null) {
			OptionWidgetManager.performApplyOptions(widgets, configuration);
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(OUTPUT_FOLDER, "");
		configuration.setAttribute(BACKEND, "");
		configuration.setAttribute(SIMULATOR, "");
		configuration.setAttribute(ACTIVATE_BACKEND, DEFAULT_BACKEND);
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	@Override
	public void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
	}

	protected void updateOptionSelection() {
		if (!plugin.isEmpty()) {
			List<OptionWidget> widgets = optionWidgets.get(plugin);
			OptionWidgetManager.hideOptions(widgets);
		}

		int index = comboPlugin.getSelectionIndex();
		if (index != -1) {
			plugin = comboPlugin.getItem(index);
			List<OptionWidget> widgets = optionWidgets.get(plugin);
			OptionWidgetManager.showOptions(widgets);
		}
	}
}
