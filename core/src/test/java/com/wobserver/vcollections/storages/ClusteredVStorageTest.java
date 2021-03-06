package com.wobserver.vcollections.storages;

import com.wobserver.vcollections.keygenerators.KeyGeneratorFactory;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClusteredVStorageTest implements StorageTest<String, String, ClusteredVStorage<String, String>> {

	private IStorage<String, String> makeStorage(IStorage<String, String> storage1, IStorage<String, String> storage2, Map<String, String> initialEntries, long maxSize) {
		IStorage<String, String> result = new ClusteredVStorage<String, String>(initialEntries, maxSize, new KeyGeneratorFactory().make(String.class), storage1, storage2);
		return result;
	}

	@Override
	public String toKey(String key) {
		return key;
	}

	@Override
	public String toValue(String value) {
		return value;
	}

	@Override
	public IStorage<String, String> makeStorage(long maxSize, Map.Entry<String, String>... entries) {
		Map<String, String> pairs = new HashMap<>();
		if (entries != null) {
			for (Map.Entry<String, String> entry : entries) {
				pairs.put(entry.getKey(), entry.getValue());
			}
		}
		IStorage<String, String> result = makeStorage(
				new MemoryStorage<String, String>(null, null, null),
				new MemoryStorage<String, String>(null, null, null),
				pairs,
				maxSize
		);
		return result;
	}

	/**
	 * <p>Given</p>: two com.wobserver.vcollections.storages, with limited capacity
	 *
	 * <p>When</p>: We try to construct a chain storage
	 *
	 * <p>Then</p> it throws {@link IllegalStateException}
	 */
	@Test
	public void shouldNotCreateForTwoUnlimitedStorages() {
		// Given
		IStorage<String, String> storage1 = new MemoryStorage<String, String>(null, null, 1L);
		IStorage<String, String> storage2 = new MemoryStorage<String, String>(null, null, 1L);

		// When
		Assertions.assertThrows(IllegalStateException.class, () -> {
			IStorage<String, String> chainStorage = new ClusteredVStorage<>(new KeyGeneratorFactory().make(String.class), storage1, storage2);
		});
	}

	/**
	 * <p>Given</p>: two com.wobserver.vcollections.storages, with inf capacities
	 *
	 * <p>When</p>: We try to construct a chain storage, with 0 capacity
	 *
	 * <p>Then</p> it throws {@link IllegalStateException}
	 */
	@Test
	public void shouldNotCreateIfTheFirstStorageIsUnlimited() {
		// Given
		IStorage<String, String> storage1 = new MemoryStorage<String, String>(null, null, null);
		IStorage<String, String> storage2 = new MemoryStorage<String, String>(null, null, null);

		// When
		Assertions.assertThrows(IllegalStateException.class, () -> {
			IStorage<String, String> chainStorage = new ClusteredVStorage<>(0L, new KeyGeneratorFactory().make(String.class), storage1, storage2);
		});
	}

	/**
	 * <p>Given</p>: no com.wobserver.vcollections.storages
	 *
	 * <p>When</p>: We try to construct a chain storage
	 *
	 * <p>Then</p> it throws {@link NotAvailableStorage}
	 */
	@Test
	public void shouldNotCreateWithoutStorage() {
		// Given

		// When
		Assertions.assertThrows(NotAvailableStorage.class, () -> {
			IStorage<String, String> chainStorage = new ClusteredVStorage<>(new KeyGeneratorFactory().make(String.class));
		});
	}

}