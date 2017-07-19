#!/usr/bin/env python
for metal in ['osmiridium', 'alumite', 'mirion', 'invar']:
	with open(metal+'_ingot_nugget.json', 'w', encoding='utf-8') as file:
		file.write('''{
	"conditions": [
		{
			"type": "plustic:item_exists",
			"item": "plustic:%(lower)singot"
		}
	],
	"type": "forge:ore_shaped",
	"pattern": [
		"III",
		"III",
		"III"
	],
	"key": {
		"I": {
			"type": "forge:ore_dict",
            "ore": "nugget%(upper)s"
		}
	},
	"result": {
		"item": "plustic:%(lower)singot"
	}
}''' % { 'upper': metal.capitalize(), 'lower': metal })
	with open(metal+'_nugget.json', 'w', encoding='utf-8') as file:
		file.write('''{
	"conditions": [
		{
			"type": "plustic:item_exists",
			"item": "plustic:%(lower)singot"
		}
	],
	"type": "forge:ore_shapeless",
	"ingredients": [
		{
            "item": "plustic:%(lower)singot"
        }
	],
	"result": {
		"item": "plustic:%(lower)snugget",
		"count": 9
	}
}''' % { 'upper': metal.capitalize(), 'lower': metal })
	with open(metal+'_ingot.json', 'w', encoding='utf-8') as file:
		file.write('''{
	"conditions": [
		{
			"type": "plustic:item_exists",
			"item": "plustic:%(lower)singot"
		}
	],
	"type": "forge:ore_shapeless",
	"ingredients": [
		{
            "item": "plustic:%(lower)sblock"
        }
	],
	"result": {
		"item": "plustic:%(lower)singot",
		"count": 9
	}
}''' % { 'upper': metal.capitalize(), 'lower': metal })
	with open(metal+'_block.json', 'w', encoding='utf-8') as file:
		file.write('''{
	"conditions": [
		{
			"type": "plustic:item_exists",
			"item": "plustic:%(lower)singot"
		}
	],
	"type": "forge:ore_shaped",
	"pattern": [
		"III",
		"III",
		"III"
	],
	"key": {
		"I": {
			"type": "forge:ore_dict",
            "ore": "ingot%(upper)s"
		}
	},
	"result": {
		"item": "plustic:%(lower)sblock"
	}
}''' % { 'upper': metal.capitalize(), 'lower': metal })