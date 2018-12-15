package com.thuisapp.mqtt.util;

import org.junit.Assert;
import org.junit.Test;

public class MqttUtilTest {

	@Test
	public void cleanSubject() {
		Assert.assertEquals("foo", MqttUtil.cleanSubject("foo"));
		Assert.assertEquals("foo", MqttUtil.cleanSubject("Foo"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("fooBar"));
		Assert.assertEquals("fooBAR", MqttUtil.cleanSubject("fooBAR"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("foo bar"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("foo_bar"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("foo-bar"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("foo Bar"));
		Assert.assertEquals("fooBar", MqttUtil.cleanSubject("foo'bar"));
		Assert.assertEquals("fooBarBaz", MqttUtil.cleanSubject("foo bar baz"));
		Assert.assertEquals("f", MqttUtil.cleanSubject("f"));
		Assert.assertEquals("", MqttUtil.cleanSubject(""));
		Assert.assertEquals(null, MqttUtil.cleanSubject(null));
	}
}
