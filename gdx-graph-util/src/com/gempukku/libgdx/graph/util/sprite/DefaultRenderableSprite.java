package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class DefaultRenderableSprite implements RenderableSprite {
    private final Vector3 position = new Vector3();
    private CullingTest cullingTest;
    private final PropertyContainer propertyContainer;

    public DefaultRenderableSprite(PropertyContainer propertyContainer) {
        this.propertyContainer = propertyContainer;
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public boolean isRendered(Camera camera) {
        return cullingTest == null || !cullingTest.isCulled(camera, getPosition());
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }
}
