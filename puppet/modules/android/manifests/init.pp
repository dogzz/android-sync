# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, you can obtain one at http://mozilla.org/MPL/2.0/.

class android {
    $android_sh = "/etc/profile.d/android.sh"
    $android_platform     = "android-17"
    $android_sdk_version  = "r21.1"

    # http://dl-ssl.google.com/android/repository/android-VERSION.zip
    $android_zip          = "android-17_r02.zip"

    # http://dl-ssl.google.com/android/repository/platform-tools_VERSION-linux.zip
    $platform_tools_zip   = "platform-tools_r16.0.2-linux.zip"

    $android_sdk_archive  = "/tmp/android-sdk_${android_sdk_version}-linux.tgz"
    $android_sdk_tmp_dir  = "/tmp/android-sdk-linux"
    $android_sdk_dir      = "/usr/local/android-sdk-linux"
    # $android_sdk_url      = "http://dl.google.com/android/android-sdk_${android_sdk_version}-linux.tgz"

    file { $android_sdk_archive:
      ensure  => present,
      source  => "puppet:///modules/data/android-sdk_${android_sdk_version}-linux.tgz",
      owner   => "vagrant",
      group   => "vagrant",
      mode    => 644,
    }

    exec { "android-sdk-untar":
      command => "tar zxf ${android_sdk_archive}",
      cwd     => "/tmp",
      creates => $android_sdk_tmp_dir,
      require => [Package["tar"], File[$android_sdk_archive]],
    }

    ->

    exec { "android":
      command => "cp -R ${android_sdk_tmp_dir} /usr/local \
&& /bin/chmod -R a+rX ${android_sdk_dir}",
      cwd     => "/tmp",
      user    => "root",
      creates => $android_sdk_dir,
    }

    # set ANDROID_HOME and PATH
    file { $android_sh:
      ensure  => present,
      content => "export PATH=\$PATH:${android_sdk_dir}/tools:${android_sdk_dir}/platform-tools
export ANDROID_HOME=${android_sdk_dir}
",
      owner   => "root",
      group   => "root",
      mode    => 644,
      require => [Exec["android"]],
    }

    file { "${android_sdk_dir}/temp":
      ensure  => directory,
      owner   => "root",
      group   => "root",
      require => [Exec["android"]],
    }

    file { "${android_sdk_dir}/temp/${android_zip}":
      ensure  => present,
      source  => "puppet:///modules/data/${android_zip}",
      owner   => "root",
      group   => "root",
      mode    => 644,
    }

    exec { "android-sdk-platform-tools":
      command => "${android_sdk_dir}/tools/android update sdk --no-ui --filter \"platform-tools\"",
      cwd     => "${android_sdk_dir}",
      user    => "root",
      creates => "${android_sdk_dir}/platform-tools/aapt",
      require => [Exec["android"], Exec["java"], File["${android_sdk_dir}/temp/${android_zip}"]],
      timeout => 0,
      logoutput => true,
    }

    file { "${android_sdk_dir}/temp/${platform_tools_zip}":
      ensure  => present,
      source  => "puppet:///modules/data/${platform_tools_zip}",
      owner   => "root",
      group   => "root",
      mode    => 644,
    }

    exec { "android-sdk-platform":
      command => "${android_sdk_dir}/tools/android update sdk --no-ui --filter \"${android_platform}\"",
      cwd     => "${android_sdk_dir}",
      user    => "root",
      creates => "${android_sdk_dir}/platforms/${android_platform}/android.jar",
      require => [Exec["android"], Exec["java"], File["${android_sdk_dir}/temp/${platform_tools_zip}"]],
      timeout => 0,
      logoutput => true,
    }
}

# Local Variables:
# mode: ruby
# tab-width: 2
# ruby-indent-level: 2
# indent-tabs-mode: nil
# End:
