package com.zzhb.zzoa.redis;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.zzhb.zzoa.config.Props;

@Service
public class RedisService {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	Props props;

	public String flushdb() {
		return stringRedisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				// 需要删除的key的数组
				List<String> delKeys = props.getFlushkeys();
				for (String string : delKeys) {
					Set<byte[]> keys = connection.keys(string.getBytes());
					for (byte[] bs : keys) {
						connection.del(bs);
					}
				}
				return "success";
			}
		});
	}

	
}
