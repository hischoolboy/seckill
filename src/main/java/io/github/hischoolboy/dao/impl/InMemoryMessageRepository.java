package io.github.hischoolboy.dao.impl;

import io.github.hischoolboy.dao.MessageRepository;
import io.github.hischoolboy.domain.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMessageRepository implements MessageRepository {

	private static AtomicLong counter = new AtomicLong();

	private final ConcurrentMap<Long, Message> messages = new ConcurrentHashMap<Long, Message>();

	@Override
	public Iterable<Message> findAll() {
		return this.messages.values();
	}

	@Override
	public Message save(Message message) {
		Long id = message.getId();
		if (id == null) {
			id = counter.incrementAndGet();
			message.setId(id);
		}
		this.messages.put(id, message);
		return message;
	}

	@Override
	public Message findMessage(Long id) {
		return this.messages.get(id);
	}

	@Override
	public void deleteMessage(Long id) {
		this.messages.remove(id);
	}
}

