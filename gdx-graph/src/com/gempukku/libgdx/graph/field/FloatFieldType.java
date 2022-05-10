package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class FloatFieldType implements ShaderFieldType, PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Number || value instanceof FloatProvider;
    }

    @Override
    public String getShaderType() {
        return "float";
    }

    @Override
    public int getNumberOfComponents() {
        return 1;
    }

    @Override
    public Object convert(Object value) {
        if (value instanceof Number)
            return ((Number) value).floatValue();
        return ((FloatProvider) value).get();
    }

    @Override
    public String getName() {
        return ShaderFieldType.Float;
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return data.getFloat("x");
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String variableName = propertySource.getUniformName();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                }, "Float property - " + propertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String variableName = propertySource.getUniformName();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                }, "Float property - " + propertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = propertySource.getAttributeName();
        vertexShaderBuilder.addAttributeVariable(attributeName, 1, getShaderType(), "Float property - " + propertySource.getPropertyName());

        return new DefaultFieldOutput(getName(), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = propertySource.getAttributeName();
        String variableName = propertySource.getVariableName();

        vertexShaderBuilder.addAttributeVariable(attributeName, 1, getShaderType(), "Float property - " + propertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, getShaderType());
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, getShaderType());
        }

        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        vertices[startIndex + 0] = ((Number) value).floatValue();
    }

    @Override
    public boolean equals(Object obj) {
        return getName().equals(((FieldType) obj).getName());
    }
}
