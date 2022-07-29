package ;

using StringTools;
function main() {
    final file : haxe.DynamicAccess<String> = haxe.Json.parse(sys.io.File.getContent("assets/emireborn/lang/en_us.json.techreborn"));
    final map : haxe.DynamicAccess<String> = {};
    for (k => v in file) {
	if (k.contains(":")) {
		final parts = k.split(":");
		if (parts.length == 2) {
			final cat = parts[1];
			final goodCat = "emi.category.techreborn." + cat;
			map.set(goodCat, v);
		}

	}
    }
    sys.io.File.saveContent("assets/emireborn/lang/en_us.json", haxe.Json.stringify(map));
    
}
