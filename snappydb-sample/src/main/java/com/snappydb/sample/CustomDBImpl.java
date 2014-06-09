package com.snappydb.sample;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.snappydb.SnappydbException;
import com.snappydb.internal.DBImpl;

public class CustomDBImpl extends DBImpl {

	public CustomDBImpl(String path) throws SnappydbException {
		super(path);
	}

	@Override
	protected Kryo getKryoInstance() {
		Kryo kryoInstance = super.getKryoInstance();
		kryoInstance.register(MyCustomObject.class, new MyCustomObjectSerializer());
		return kryoInstance;
	}

	class MyCustomObjectSerializer extends Serializer<MyCustomObject> {
		@Override
		public void write(Kryo kryo, Output output, MyCustomObject object) {
			output.writeString(object.getMyArg());
		}

		@Override
		public MyCustomObject read(Kryo kryo, Input input, Class type) {
			return new MyCustomObject(input.readString());
		}
	}

}
