package com.hal.annotation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("annotation.Assignment")
public class AssignmentProcess extends AbstractProcessor {
	private TypeElement assignmentElement;
	
	public synchronized void init(ProcessingEnvironment processingEnv) {
		// TODO Auto-generated method stub
		super.init(processingEnv);
		Elements element = processingEnv.getElementUtils();
		assignmentElement = element.getTypeElement("annotation.Assignment");
	}
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// TODO Auto-generated method stub
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(assignmentElement);
		for (Element element : elements) {
			processAssignment(element);
		}
	}

	private void processAssignment(Element element){
		List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
		for (AnnotationMirror mirror : annotations) {
			if (mirror.getAnnotationType().asElement().equals(assignmentElement)) {
				Map<? extends ExecutableElement, ? extends AnnotationValue> map = mirror.getElementValues();
				String value = (String)getAnnotationValue(map, "assignee");
			}
		}
	}
	private Object getAnnotationValue(Map<? extends ExecutableElement, ? extends AnnotationValue> map ,String name){
		AnnotationValue values = map.get(assignmentElement);
		Class<?> clazz = values.getClass();
		String  value = null;
		try {
			  value= clazz.getDeclaredMethod(name, String.class).getName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return value;
	}
}
