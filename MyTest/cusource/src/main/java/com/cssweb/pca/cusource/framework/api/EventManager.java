package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.api.http.EventGateWay;
import cn.unicompay.wallet.client.framework.api.http.model.GetAllEventListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetBrandListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetBrandListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetEventDetailRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetEventDetailRs;
import cn.unicompay.wallet.client.framework.api.http.model.Result;
import cn.unicompay.wallet.client.framework.model.Brand;
import cn.unicompay.wallet.client.framework.model.Event;
import cn.unicompay.wallet.client.framework.model.EventStore;
import cn.unicompay.wallet.client.framework.util.Util;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;

public class EventManager {
	private WApplication context;

	public EventManager(WApplication context) {
		this.context = context;
	}

	public void getAllEvent(final EventListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				EventGateWay eventGw = context.getNetworkManager()
						.getEventGateWay();
				try {
					final GetAllEventListRs res = eventGw.getAllEventList();
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							@Override
							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {

									Vector<Event> list = res.getEventList();
									l.onList(list == null ? new Vector<Event>(0)
											: list);
								} 
							}
						});
					}
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							@Override
							public void run() {
								l.onNoNetwork();
							}
						});
						return;
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.onNoResponse();
							}
						});
						return;
					}
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onError(e.getCode(),e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	public void getEventDetail(final String eventId, final String regionName,
			final EventDetailListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				GetEventDetailRq rq = new GetEventDetailRq();
				rq.setEventId(eventId);
				rq.setRegionName(regionName);
				EventGateWay eventGw = context.getNetworkManager()
						.getEventGateWay();
				try {
					final GetEventDetailRs res = eventGw.getEventDetail(rq);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							@Override
							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {
									Event event = new Event();
									event.setEventDetail(res.getEventDetail());
									event.setEventDetailImageUrl(res
											.getEventDetailImageUrl());
									l.onDetail(event);
									Vector<EventStore> r = res.getStoreList();
									l.onList(r == null ? new Vector<EventStore>(
											0) : r);
								} 
							}
						});
					}
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							@Override
							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							@Override
							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}

			}
		}).start();
	}

	public void getEventBrand(final EventBrandListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				GetBrandListRq rq = new GetBrandListRq();
				// rq.setWalletId("");
				EventGateWay eventGW = context.getNetworkManager()
						.getEventGateWay();

				try {
					final GetBrandListRs res = eventGW.getBrandList(rq);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {
									Vector<Brand> r = res.getList();
									l.onList(r == null ? new Vector<Brand>(0)
											: r);
								} else {
									l.onError();
								}
							}
						});
					}
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onError();
						}
					});
					return;
				}

			}
		}).start();
	}
}
