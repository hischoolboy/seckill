package io.github.hischoolboy.dao;

import io.github.hischoolboy.domain.Message;

public interface MessageRepository {

	Iterable<Message> findAll();

	Message save(Message message);

	Message findMessage(Long id);

	void deleteMessage(Long id);

}
