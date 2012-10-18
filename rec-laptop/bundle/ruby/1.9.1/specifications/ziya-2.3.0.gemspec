# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "ziya"
  s.version = "2.3.0"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Fernand Galiana"]
  s.date = "2010-10-13"
  s.description = "ZiYa allows you to easily create interactive charts, gauges and maps for your web applications. ZiYa leverages\nflash which offload heavy server side processing to the client. At the root ZiYa allows you to easily generate an\nXML files that will be downloaded to the client for rendering. Using this gem, you will be able to easily create great\nlooking charts for your application. You will also be able to use the charts, gauges and maps has a navigation scheme \nby embedding various link in the graphical components thus bringing to the table an ideal scheme for reporting and dashboard\nlike applications. Your manager will love you for it !!\n\n\tSample site   : http://ziya.liquidrail.com\n\tDocumentation : http://ziya.liquidrail.com/docs\n\tForum         : http://groups.google.com/group/ziya-plugin\n\tRepositories  : git://github.com/derailed/ziya.git"
  s.email = "fernand@liquidrail.com"
  s.executables = ["ziyafy"]
  s.extra_rdoc_files = ["History.txt", "README.rdoc", "bin/ziyafy", "examples/charts/public/charts/themes/readme.txt", "examples/maps/public/maps/themes/readme.txt", "release_notes.txt", "resources/charts/themes/readme.txt", "resources/maps/themes/readme.txt", "version.txt"]
  s.files = ["bin/ziyafy", "History.txt", "README.rdoc", "examples/charts/public/charts/themes/readme.txt", "examples/maps/public/maps/themes/readme.txt", "release_notes.txt", "resources/charts/themes/readme.txt", "resources/maps/themes/readme.txt", "version.txt"]
  s.homepage = "http://ziya.liquidrail.com/"
  s.rdoc_options = ["--main", "README.rdoc"]
  s.require_paths = ["lib"]
  s.rubyforge_project = "ziya"
  s.rubygems_version = "1.8.11"
  s.summary = "ZiYa allows you to easily create interactive charts, gauges and maps for your web applications"

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_development_dependency(%q<bones>, [">= 3.4.7"])
    else
      s.add_dependency(%q<bones>, [">= 3.4.7"])
    end
  else
    s.add_dependency(%q<bones>, [">= 3.4.7"])
  end
end
