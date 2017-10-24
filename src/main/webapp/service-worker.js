var CACHE_NAME = "admin-cache-v1";

self.addEventListener("install", function(event) {
	event.waitUntil(
		caches.open(CACHE_NAME).then(function(cache) {
			console.log('install event ');
		})
	);
});

self.addEventListener("fetch", function(event) {
	console.log("Fetch event for ", event.request.url);
	event.respondWith(
		fetch(event.request).then(function(resp) {
			/*var respClone = resp.clone();
			if (resp.ok) {
				console.log("OK, adding to cache ", event.request.url);
				caches.open(CACHE_NAME).then(function(cache) {
					cache.put(event.request, respClone);
					return resp;
				});
				return resp;
			} else {
				console.log("NOK, getting from cache ", event.request.url, " status ", resp.status, " text ", resp.statusText);
				return caches.match(event.request);
			}*/
		}).catch(function(error) {
			console.log("ERROR, getting from cache ", event.request.url, " error ", error);
			//return caches.match(event.request);
		})
	);
});