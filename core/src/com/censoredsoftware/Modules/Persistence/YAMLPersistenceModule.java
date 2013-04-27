package com.censoredsoftware.Modules.Persistence;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Modules.Data.DataModule;
import com.censoredsoftware.Modules.Data.DataStubModule;
import com.censoredsoftware.Modules.Persistence.Event.LoadStubYAMLEvent;
import com.censoredsoftware.Modules.Persistence.Event.LoadYAMLEvent;

/**
 * Module to handle all saving related methods.
 */
public class YAMLPersistenceModule
{
	private Plugin plugin;
	private String path, pluginName;
	final private String dataName;
	private Map map;
	final private File yamlFile, backupFile;
	private FileConfiguration persistance;
	private Logger log = Logger.getLogger("Minecraft");

	/**
	 * Constructor for creating the PlayerCharacterYAML.
	 * 
	 * @param instance The instance of the Plugin creating the PlayerCharacterYAML.
	 * @param dataName The name of data set, and the name of the file.
	 */
	public YAMLPersistenceModule(boolean load, Plugin instance, String path, String dataName)
	{
		this.plugin = instance;
		this.path = path;
		this.pluginName = this.plugin.getName();
		this.dataName = dataName;

		folderStructure(this.path);

		this.yamlFile = new File(plugin.getDataFolder() + File.separator + path + File.separator + dataName + ".yml");
		this.backupFile = new File(plugin.getDataFolder() + path + File.separator + "backup" + File.separator + dataName + ".yml");

		initialize(load);
	}

	/**
	 * Make sure the folder structure is in place.
	 */
	private void folderStructure(String path)
	{
		// Define the folders
		File dataFolder = new File(plugin.getDataFolder() + File.separator + path);
		File backupFolder = new File(plugin.getDataFolder() + File.separator + "backup" + File.separator + path);

		// If they don't exist, create them now
		if(!dataFolder.exists()) dataFolder.mkdirs();
		if(!backupFolder.exists()) backupFolder.mkdirs();
	}

	/**
	 * Initialize the PlayerCharacterYAML.
	 */
	private void initialize(boolean load)
	{
		if(!this.yamlFile.exists())
		{
			try
			{
				this.yamlFile.createNewFile();
				this.persistance = YamlConfiguration.loadConfiguration(yamlFile);
			}
			catch(Exception e)
			{
				log.severe("[" + pluginName + "] Unable to create save file: " + dataName + ".yml");
				log.severe("[" + pluginName + "] Error: " + e);
				log.severe("[" + pluginName + "] Please check your write permissions and try again.");
			}
			return;
		}
		else
		{
			this.persistance = YamlConfiguration.loadConfiguration(yamlFile);
			if(load) load();
		}
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	public boolean save(DataModule dataModule)
	{
		// Grab the latest map for saving
		this.map = dataModule.getMap();

		try
		{
			this.persistance.createSection(dataName, this.map);
			if(this.backupFile.exists()) this.backupFile.delete();
			this.yamlFile.renameTo(this.backupFile);
			this.yamlFile.createNewFile();
			this.persistance.save(this.yamlFile);
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + pluginName + "] Unable to save file: " + dataName + ".yml");
			log.severe("[" + pluginName + "] Error: " + e);
			log.severe("[" + pluginName + "] Please check your write permissions and try again.");
		}
		return false;
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	public boolean save(DataStubModule stub)
	{
		// Grab the latest map for saving
		this.map = stub.getMap();

		try
		{
			this.persistance.createSection(String.valueOf(stub.getID()), this.map);
			if(this.backupFile.exists()) this.backupFile.delete();
			this.yamlFile.renameTo(this.backupFile);
			this.yamlFile.createNewFile();
			this.persistance.save(this.yamlFile);
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + pluginName + "] Unable to save file: " + dataName + ".yml");
			log.severe("[" + pluginName + "] Error: " + e);
			log.severe("[" + pluginName + "] Please check your write permissions and try again.");
		}
		return false;
	}

	/**
	 * Save the data that this module handles.
	 */
	public boolean save(List stubs)
	{
		boolean works = true;
		for(Object stub : stubs)
		{
			if(stub instanceof DataStubModule)
			{
				if(!save((DataStubModule) stub)) works = false;
			}
		}
		return works;
	}

	/**
	 * Revert to the backup file.
	 */
	public void revertBackup()
	{
		backupFile.renameTo(yamlFile);
	}

	/**
	 * Load the data from the YAML file.
	 * 
	 * @return The data as a Map.
	 */
	public void load()
	{
		// Prevent NullPointerException Error
		if(this.persistance == null) return;

		loadStubs();

		// Prevent NullPointerException Error
		if(this.persistance.getConfigurationSection(dataName) == null) return;

		// Define variables
		Map map = new HashMap();
		int error = 0;

		// Loop through the data in the file
		for(String key : this.persistance.getConfigurationSection(dataName).getKeys(false))
		{
			try
			{
				Object data = this.persistance.get(dataName + "." + key);
				if(data instanceof ConfigurationSection)
				{
					Map map_ = new HashMap();
					for(String key_ : ((ConfigurationSection) data).getKeys(false))
					{
						Object data_ = this.persistance.get(dataName + "." + key + "." + key_);
						map_.put(key_, data_);
					}
					data = map_;
				}
				map.put(key, data);
			}
			catch(Exception e)
			{
				error++;
				log.severe("[" + pluginName + "] Unable to load from file: " + dataName + ".yml");
				log.severe("[" + pluginName + "] Error: " + e);
			}
		}

		// Call the LoadYAMLEvent if need be
		if(!map.isEmpty() && error == 0) plugin.getServer().getPluginManager().callEvent(new LoadYAMLEvent(pluginName, path, dataName, map));
	}

	private void loadStubs()
	{
		for(String tier : this.persistance.getKeys(false))
		{
			if(!DemigodsData.isInt(tier) || tier.equals(dataName)) continue;

			// Define variables
			Map map = new HashMap();
			int error = 0;

			// Loop through the data in the file
			for(String key : this.persistance.getConfigurationSection(tier).getKeys(false))
			{
				try
				{
					Object data = this.persistance.get(tier + "." + key);
					map.put(key, data);
				}
				catch(Exception e)
				{
					error++;
					log.severe("[" + pluginName + "] Unable to load from file: " + dataName + ".yml");
					log.severe("[" + pluginName + "] Error: " + e);
				}
			}

			// Call the LoadStubYAMLEvent if need be
			if(!map.isEmpty() && error == 0) plugin.getServer().getPluginManager().callEvent(new LoadStubYAMLEvent(pluginName, path, dataName, Integer.parseInt(tier), map));
		}
	}
}
