package com.eternalcode.eternaleconomy.configuration.implementation;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
@Header("# ")
@Header("# EternalEconomy configuration file")
@Header("# Permissions:")
@Header("# - eternaleconomy.admin - admin permissions")
@Header("# - eternaleconomy.player - player permissions")
@Header("# ")
public class PluginConfiguration extends OkaeriConfig {
}
