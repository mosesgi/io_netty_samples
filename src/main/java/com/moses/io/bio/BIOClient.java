package com.moses.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class BIOClient {

	public static void main(String[] args) throws InterruptedException {
		int count = 100;

		final CountDownLatch latch = new CountDownLatch(count);

		for (int i = 0; i < count; i++) {
			new Thread() {

				@Override
				public void run() {
					try {

						Socket client = new Socket("localhost", 8080);
						OutputStream os = client.getOutputStream();
						String name = UUID.randomUUID().toString();

						os.write(name.getBytes());
						os.close();
						client.close();

						latch.countDown();
					} catch (Exception e) {

					}
				}

			}.start();
		}

		latch.await();
	}
}
