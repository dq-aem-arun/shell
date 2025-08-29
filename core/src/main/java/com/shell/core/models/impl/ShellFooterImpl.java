package com.shell.core.models.impl;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.FooterItem;
import com.shell.core.models.ShellFooter;


@Model(adaptables = Resource.class, adapters = ShellFooter.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShellFooterImpl implements ShellFooter {

        public static Logger LOG = LoggerFactory.getLogger(ShellFooterImpl.class);

        @ChildResource
        private List<FooterItem> footerItem;

        public static Logger getLOG() {
                return LOG;
        }

        public List<FooterItem> getFooterItem() {
                return footerItem;
        }

        public boolean isEmpty() {
                return footerItem == null || footerItem.isEmpty();
        }
        
}