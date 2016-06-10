package com.dcoc.unravel.server.config;

import com.dcoc.unravel.server.utils.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigLoader {

	private static Pattern IMPORT = Pattern.compile("\"@import\\s+([a-zA-Z0-9.\\-_/]+)\"");

	private static enum Type{
		RESOURCE,FILE
	}

	private final Type type;
	private final String  root;
	private final String  file;

	private Map<String,Object> config;

	public ConfigLoader(String path) {
		int di = path.indexOf(':');
		int fi = path.lastIndexOf('/');

		this.type = Type.valueOf(path.substring(0, di).toUpperCase());
		this.root = path.substring(di + 1, fi + 1);
		this.file = path.substring(fi+1);
	}

	public Map<String,Object> load() {
		return config = JSON.toMap(read());
	}

	public Map<String,Object> patch(String path) {
		return merge(config, new ConfigLoader(path).load());
	}

	public Map<String,Object> flat() {
		return flat(config, null);
	}

	public Map<String,Object> flat(String path) {
		return flat(config, path);
	}

	public String read() {
		String content = null;
		switch (type) {
			case FILE : content = loadFile(root+file);break;
			case RESOURCE : content = loadResource(root + file);break;
		}
		if (content != null && content.length() > 0) {
			Matcher m = IMPORT.matcher(content);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				String path = resolvePath(root+m.group(1));
				if (path.endsWith(".json")) {
					m.appendReplacement(sb,
						Matcher.quoteReplacement(new ConfigLoader(
							type.name().toLowerCase()+":"+path
						).read())
					);
				} else {
					switch (type) {
						case FILE       :
							m.appendReplacement(sb, Matcher.quoteReplacement(JSON.encode(loadFile(path))));
						break;
						case RESOURCE   :
							m.appendReplacement(sb, Matcher.quoteReplacement(JSON.encode(loadResource(path))));
						break;
					}
				}
			}
			m.appendTail(sb);
			return sb.toString();
		}else{
			return "null";
		}
	}

	private Map<String,Object> merge(Map<String,Object> source, Map<String,Object> target) {
		if (target!=null) {
			for (Map.Entry<String,Object> entry:target.entrySet()) {
				String key = entry.getKey();
				Object val = entry.getValue();
				if (Map.class.isAssignableFrom(val.getClass())) {
					Object sval = source.get(key);
					if (sval!=null && Map.class.isAssignableFrom(sval.getClass())) {
						merge((Map<String,Object>)sval,(Map<String,Object>)val);
					} else {
						source.put(key,val);
					}
				} else {
					source.put(key,val);
				}
			}
		}
		return source;
	}

	public Map<String,Object> flat(Map<String,Object> source,String path) {
		Map<String,Object> target = new LinkedHashMap<String,Object>();
		if (source != null) {
			for (Map.Entry<String,Object> entry:source.entrySet()) {
				String key = entry.getKey();
				Object val = entry.getValue();
				if (val != null && Map.class.isAssignableFrom(val.getClass())) {
					Map<String,Object>  vmap = (Map<String,Object>)val;
					String              vkey = path==null?key:path+"."+key;
					target.put(vkey,val);
					target.putAll(flat(vmap,vkey));
				} else {
					target.put(path+"."+key,val);
				}
			}
		}
		return target;
	}

	private String loadResource(String path) {
		try {
			ClassLoader loader = ConfigLoader.class.getClassLoader();
			InputStream is = loader.getResourceAsStream(path);
			InputStreamReader reader = new InputStreamReader(is);
			String data = CharStreams.toString(reader);
			reader.close();
			return data;
		} catch(Exception ex) {
			System.out.println(path);
			ex.printStackTrace();
			return null;
		}
	}

	private String resolvePath(String str) {
		Stack<String> stack = new Stack<String>();
		String[] path = str.split("/");
		for (String l:path) {
			if (l.equals("..")) {
				stack.pop();
			} else if(!l.equals(".")) {
				stack.push(l);
			}
		}
		
		return Joiner.on('/').join(stack.toArray());
	}

	private String loadFile(String path) {
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				try {
					return CharStreams.toString(Files.newReaderSupplier(file, Charsets.UTF_8));
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}
}
