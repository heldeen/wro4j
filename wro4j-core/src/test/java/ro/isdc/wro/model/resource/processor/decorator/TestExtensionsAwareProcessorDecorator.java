/**
 * Copyright wro4j@2011
 */
package ro.isdc.wro.model.resource.processor.decorator;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.TestProcessorsUtils;
import ro.isdc.wro.model.resource.processor.impl.css.CssUrlRewritingProcessor;
import ro.isdc.wro.model.resource.processor.impl.js.JSMinProcessor;
import ro.isdc.wro.util.WroTestUtils;

/**
 * @author Alex Objelean
 */
public class TestExtensionsAwareProcessorDecorator {
  @Before
  public void setUp() {
    Context.set(Context.standaloneContext());
  }

  @Test
  public void shouldApplyProcessorOnlyOnResourcesWithExtensionJs()
      throws Exception {
    final ResourcePreProcessor decoratedProcessor = new JSMinProcessor();
    final ResourcePreProcessor processor = ExtensionsAwareProcessorDecorator.decorate(decoratedProcessor).addExtension(
        "js");
    //we use test resource relative to TestProcessorsUtils class
    final URL url = TestProcessorsUtils.class.getResource("extensionAware");

    final File testFolder = new File(url.getFile(), "test");
    final File expectedFolder = new File(url.getFile(), "expected");
    WroTestUtils.compareFromDifferentFolders(testFolder, expectedFolder, processor);
  }

  @Test(expected=NullPointerException.class)
  public void cannotAcceptNullExtension() {
    final ResourcePreProcessor decoratedProcessor = new JSMinProcessor();
    ExtensionsAwareProcessorDecorator.decorate(decoratedProcessor).addExtension(null);
  }

  @Test
  public void testMinimizeAwareDecorator1() {
    final ResourcePreProcessor decoratedProcessor = new JSMinProcessor();
    final ResourcePreProcessor processor = ExtensionsAwareProcessorDecorator.decorate(decoratedProcessor);
    Assert.assertTrue(new ProcessorDecorator(processor).isMinimize());
  }

  @Test
  public void testMinimizeAwareDecorator2() {
    final ResourcePreProcessor decoratedProcessor = new CssUrlRewritingProcessor();
    final ResourcePreProcessor processor = ExtensionsAwareProcessorDecorator.decorate(decoratedProcessor);
    Assert.assertFalse(new ProcessorDecorator(processor).isMinimize());
  }
}
