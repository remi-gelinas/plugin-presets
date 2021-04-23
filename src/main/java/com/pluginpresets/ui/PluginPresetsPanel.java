/*
 * Copyright (c) 2021, antero111 <https://github.com/antero111>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pluginpresets.ui;

import com.pluginpresets.PluginPresetsPlugin;
import com.pluginpresets.PluginPreset;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.ImageUtil;

class PluginPresetsPanel extends JPanel
{
	private static final Border NAME_BOTTOM_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
		BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR));

	private static final ImageIcon SWITCH_ON_ICON;
	private static final ImageIcon SWITCH_ON_HOVER_ICON;

	private static final ImageIcon SWITCH_OFF_ICON;
	private static final ImageIcon SWITCH_OFF_HOVER_ICON;

	private static final ImageIcon OVERWRITE_ICON;
	private static final ImageIcon OVERWRITE_HOVER_ICON;
	private static final ImageIcon DELETE_ICON;
	private static final ImageIcon DELETE_HOVER_ICON;

	private final PluginPresetsPlugin plugin;

	private final PluginPreset preset;

	private final JLabel loadLabel = new JLabel();
	private final JLabel overwriteLabel = new JLabel();
	private final JLabel deleteLabel = new JLabel();

	private final FlatTextField nameInput = new FlatTextField();
	private final JLabel save = new JLabel("Save");
	private final JLabel cancel = new JLabel("Cancel");
	private final JLabel rename = new JLabel("Rename");

	static
	{
		final BufferedImage switchOnImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "switch_on_icon.png");
		SWITCH_ON_ICON = new ImageIcon(switchOnImg);
		SWITCH_ON_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(switchOnImg, -10));

		final BufferedImage switchOffImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "switch_off_icon.png");
		final BufferedImage switchOffHoverImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "switch_off_hover_icon.png");
		SWITCH_OFF_ICON = new ImageIcon(switchOffImg);
		SWITCH_OFF_HOVER_ICON = new ImageIcon(switchOffHoverImg);

		final BufferedImage overwriteImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "overwrite_icon.png");
		OVERWRITE_ICON = new ImageIcon(overwriteImg);
		OVERWRITE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(overwriteImg, -100));

		final BufferedImage deleteImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "delete_icon.png");
		DELETE_ICON = new ImageIcon(deleteImg);
		DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));
	}

	PluginPresetsPanel(PluginPreset pluginPreset, PluginPresetsPlugin pluginPresetsPlugin)
	{
		this.plugin = pluginPresetsPlugin;
		this.preset = pluginPreset;

		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel nameWrapper = new JPanel(new BorderLayout());
		nameWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameWrapper.setBorder(NAME_BOTTOM_BORDER);

		JPanel nameActions = new JPanel(new BorderLayout(3, 0));
		nameActions.setBorder(new EmptyBorder(0, 0, 0, 8));
		nameActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		save.setVisible(false);
		save.setFont(FontManager.getRunescapeSmallFont());
		save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		save.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				save();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR.darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
			}
		});

		cancel.setVisible(false);
		cancel.setFont(FontManager.getRunescapeSmallFont());
		cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		cancel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				cancel();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR.darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
			}
		});

		rename.setFont(FontManager.getRunescapeSmallFont());
		rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
		rename.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				nameInput.setEditable(true);
				updateNameActions(true);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker().darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
			}
		});

		nameActions.add(save, BorderLayout.EAST);
		nameActions.add(cancel, BorderLayout.WEST);
		nameActions.add(rename, BorderLayout.CENTER);

		nameInput.setText(preset.getName());
		nameInput.setBorder(null);
		nameInput.setEditable(false);
		nameInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameInput.setPreferredSize(new Dimension(0, 24));
		nameInput.getTextField().setForeground(Color.WHITE);
		nameInput.getTextField().setBorder(new EmptyBorder(0, 8, 0, 0));
		nameInput.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					save();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					cancel();
				}
			}
		});

		nameWrapper.add(nameInput, BorderLayout.CENTER);
		nameWrapper.add(nameActions, BorderLayout.EAST);

		JPanel bottomContainer = new JPanel(new BorderLayout());
		bottomContainer.setBorder(new EmptyBorder(6, 0, 6, 0));
		bottomContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
		leftActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		if (preset.getSelected())
		{
			loadLabel.setToolTipText("Current preset");
			loadLabel.setIcon(SWITCH_ON_ICON);
			loadLabel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent mouseEvent)
				{
					plugin.loadPreset(preset);
					plugin.setAsSelected(preset);
				}

				@Override
				public void mouseEntered(MouseEvent mouseEvent)
				{
					loadLabel.setIcon(SWITCH_ON_HOVER_ICON);
				}

				@Override
				public void mouseExited(MouseEvent mouseEvent)
				{
					loadLabel.setIcon(SWITCH_ON_ICON);
				}
			});
		}
		else
		{
			loadLabel.setToolTipText("Load this preset");
			loadLabel.setIcon(SWITCH_OFF_ICON);
			loadLabel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent mouseEvent)
				{
					plugin.loadPreset(preset);
					plugin.setAsSelected(preset);
				}

				@Override
				public void mouseEntered(MouseEvent mouseEvent)
				{
					loadLabel.setIcon(SWITCH_OFF_HOVER_ICON);
				}

				@Override
				public void mouseExited(MouseEvent mouseEvent)
				{
					loadLabel.setIcon(SWITCH_OFF_ICON);
				}
			});
		}

		leftActions.add(loadLabel);

		JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		overwriteLabel.setIcon(OVERWRITE_ICON);
		overwriteLabel.setToolTipText("Overwrite preset");
		overwriteLabel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				int confirm = JOptionPane.showConfirmDialog(PluginPresetsPanel.this,
					"Are you sure you want to overwrite this preset with your current plugin configurations?",
					"Overwrite preset", JOptionPane.OK_CANCEL_OPTION);

				if (confirm == 0)
				{
					plugin.overwritePreset(preset);
				}
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				overwriteLabel.setIcon(OVERWRITE_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				overwriteLabel.setIcon(OVERWRITE_ICON);
			}
		});

		deleteLabel.setIcon(DELETE_ICON);
		deleteLabel.setToolTipText("Delete preset");
		deleteLabel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				int confirm = JOptionPane.showConfirmDialog(PluginPresetsPanel.this,
					"Are you sure you want to permanently delete this plugin preset?",
					"Delete preset", JOptionPane.OK_CANCEL_OPTION);

				if (confirm == 0)
				{
					plugin.deletePreset(preset);
				}
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				deleteLabel.setIcon(DELETE_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				deleteLabel.setIcon(DELETE_ICON);
			}
		});

		rightActions.add(overwriteLabel);
		rightActions.add(deleteLabel);

		bottomContainer.add(leftActions, BorderLayout.WEST);
		bottomContainer.add(rightActions, BorderLayout.EAST);

		add(nameWrapper, BorderLayout.NORTH);
		add(bottomContainer, BorderLayout.CENTER);
	}

	private void save()
	{
		if (nameInput.getText().equals("") || plugin.stringContainsInvalidCharacters(nameInput.getText()))
		{
			String defaultPresetName = PluginPresetsPlugin.getDEFAULT_PRESET_NAME();
			preset.setName(defaultPresetName);
			nameInput.setText(defaultPresetName);
		}
		else
		{
			preset.setName(nameInput.getText());
		}
		plugin.savePresets();

		nameInput.setEditable(false);
		updateNameActions(false);
		requestFocusInWindow();
	}

	private void cancel()
	{
		nameInput.setEditable(false);
		nameInput.setText(preset.getName());
		updateNameActions(false);
		requestFocusInWindow();
	}

	private void updateNameActions(boolean saveAndCancel)
	{
		save.setVisible(saveAndCancel);
		cancel.setVisible(saveAndCancel);
		rename.setVisible(!saveAndCancel);

		if (saveAndCancel)
		{
			nameInput.getTextField().requestFocusInWindow();
			nameInput.getTextField().selectAll();
		}
	}
}