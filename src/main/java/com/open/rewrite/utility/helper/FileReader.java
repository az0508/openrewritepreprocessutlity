package com.open.rewrite.utility.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class FileReader {

	@Value("${git.branch.name.openrewrite.recipe.name.sync}")
	private String git_branch_name_openrewrite_recipe_name_sync;

	@Autowired
	Environment env;

	Map<String, Object> map = new HashMap();
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	
	public void load() {
		if (env instanceof ConfigurableEnvironment) {
			for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
				if (propertySource instanceof EnumerablePropertySource) {
					for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
						map.put(key, propertySource.getProperty(key));
					}
				}
			}
		}
	}

	public void syncRepos(File[] files) {
		var i = 0;
		var GBASH_COMMAND = "git checkout";         
		for (File file : files) {
			if (file.isDirectory()) {
				try {
					Process process = Runtime.getRuntime().exec(
							"" + GBASH_COMMAND + " -b " + git_branch_name_openrewrite_recipe_name_sync + "", null,
							new File(file.getAbsolutePath()));
					logger.info("process initited count " + i++ + " " + process.isAlive() + " - "+file.getName());
					process.waitFor();
					int exitValue = process.exitValue();
					logger.info("exitValue " + exitValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

			}
		}
	}

	public String getGit_branch_name_openrewrite_recipe_name_sync() {
		return git_branch_name_openrewrite_recipe_name_sync;
	}

	public void setGit_branch_name_openrewrite_recipe_name_sync(String git_branch_name_openrewrite_recipe_name_sync) {
		this.git_branch_name_openrewrite_recipe_name_sync = git_branch_name_openrewrite_recipe_name_sync;
	}

}
